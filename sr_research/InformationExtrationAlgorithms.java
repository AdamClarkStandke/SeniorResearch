package sr_research;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.codec.binary.Base64;
import com.opencsv.CSVReader;

//import org.junit.Test;



public class InformationExtrationAlgorithms {

	//instance variables used for link-target identification and CoreEx
	private static String gallery_pattern = "\\bgallery?";
	private static String video_pattern = "\\bvideos?";
    private static String image_pattern = "\\bimages?";
    private static String photo_pattern = "\\bphotos?";
    private static String slideshow_pattern = "\\bslideshows?";
    private static String episode_pattern = "\\bepisodes?";
    private static String player_pattern = "\\bplayers?";
    private static String comment_pattern = "\\bcomments?";
    private static String id_pattern = "id=[\\d]+";
    private static String numberRegrex= "[-]\\d{5,}[-/]?|[/]\\d{5,}[/]"; 
    private static String dateRegrex ="\\d{2,4}[-/]\\d{1,2}[-/]\\d{1,4}|[/]\\d{8}[-]";
    private static Pattern gallery; 
    private static Pattern video;
    private static Pattern image;
    private static Pattern photo;
    private static Pattern slideshow;
    private static Pattern episode;
    private static Pattern player;
    private static Pattern comment;
    private static Pattern idpattern; 
    
    /**
     * Constructor that compiles the case-sensitive regrex pattern once, 
     * so that it does not have to be recompiled multiple times for each web page 
     */
	public InformationExtrationAlgorithms() 
	{
		 gallery = Pattern.compile(gallery_pattern, Pattern.CASE_INSENSITIVE); 
		 video = Pattern.compile(video_pattern, Pattern.CASE_INSENSITIVE);
		 image = Pattern.compile(image_pattern, Pattern.CASE_INSENSITIVE);
		 photo = Pattern.compile(photo_pattern, Pattern.CASE_INSENSITIVE);
		 slideshow = Pattern.compile(slideshow_pattern, Pattern.CASE_INSENSITIVE);
		 episode = Pattern.compile(episode_pattern, Pattern.CASE_INSENSITIVE);
		 player = Pattern.compile(player_pattern, Pattern.CASE_INSENSITIVE);
		 comment = Pattern.compile(comment_pattern, Pattern.CASE_INSENSITIVE);
		 idpattern = Pattern.compile(id_pattern, Pattern.CASE_INSENSITIVE);
	}

	/**
	 * Main method that opens the file directory where 
	 * each html file is stored(ie., the data directory).
	 * After opening the directory, each filename is decoded from
	 * base 64 to its url name. Then each information extraction
	 * algorithm is called for further processing. Namely, Link 
	 * Target Identification and CoreEx.   
	 * @param args optional command line arguments
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void main(String[] args) throws IOException 
	{
		new InformationExtrationAlgorithms(); 
		File folder = new File("/Users/adam/eclipse-workspace/sr_research/sr_research_498_adam_new_project/going_headless/data/"); 
		File [] listOfFiles = folder.listFiles(); 
		for(File file: listOfFiles)
		{
			if(file.isFile())
			{
				String rm_html = file.getName(); 
				rm_html=rm_html.replace(".html", ""); 
				byte[] decoded = Base64.decodeBase64(rm_html);
				String root_url= new String(decoded, "UTF-8");
				System.out.println(root_url);
//				LinkTargetIdentification(root_url);
//				CorexEx(file, root_url); 
			}
		}
		
 

	}
	
	/**
	 * Method that will implement link target identification. Each root url
	 * will be parsed based on the following features: does the root link contain a number or Id 
	 * in the name of the link, 
	 * does the root link contain a date, does the root link contain a reserved word, 
	 * does the root link  end with a forward slash mark, 
	 * the length of the root link, and the number of forward slashes contained in the root link.  
	 * @param baseurl the root url that will be parsed for information
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void LinkTargetIdentification(String baseurl) throws IOException 
	{   
		//features to extract for each link
		boolean num_or_id=false; 
		boolean date =false; 
		boolean reservedWord =false; 
		boolean endWithSlash = false; 
		int lenghLink =0; 
		int numForwardSlashes = 0; 
		
		//pre-processes url to get rid of boilerplate terms of https:// or http://
		StringBuffer https = new StringBuffer("https://"); 
		StringBuffer http  = new StringBuffer("http://"); 
		if(baseurl.contains(https))
		{
			baseurl=baseurl.replaceFirst("https://", ""); 
		}
		else if (baseurl.contains(http))
		{
			baseurl=baseurl.replaceFirst("http://", "");

		}
		
		//checks to see if link ends with slash
		if(baseurl.endsWith("/"))
		{
			endWithSlash=true; 
		}
		
		//computes the approximate length of the link 
		lenghLink=baseurl.length(); 
		
		//checks to see if date is in the format like /mdy/-mdy/-mdy/ where m=month,d=day, y=year
		if (baseurl.matches(dateRegrex))
		{
			date=true; 
		}
		
		//counts the number of forwards slash marks in a link
		String[] array = baseurl.split("/");
		numForwardSlashes=array.length;
		
		//checks to see if link contains a Id in the name link 
		if(idpattern.matcher(baseurl).matches())
		{
			num_or_id=true; 
		}
		
		//checks to see if link contains a contiguous number in the name link
	    if (baseurl.matches(numberRegrex))  
		{
	    	num_or_id=true;  
	 
		}
	    
	    //will determine if link has a reserved word in its link as dictated by the literature
		if(gallery.matcher(baseurl).matches())
		{
			reservedWord=true; //checks if link has gallery word in link
		}
		 
		if(video.matcher(baseurl).matches())
		{
			reservedWord=true; //checks if link has video word in link
		}
		
		if(image.matcher(baseurl).matches())
		{
			reservedWord=true; //checks if link has image word in link
		}
		 
		if(photo.matcher(baseurl).matches())
		{
			reservedWord=true; //checks if link has photo word in link
		}
		 
		if(slideshow.matcher(baseurl).matches())
		{
			reservedWord=true; //checks if link has slide-show word in link
		}

		if(episode.matcher(baseurl).matches())
		{
			reservedWord=true; //checks if link has episode word in link
		}
		 
		if(player.matcher(baseurl).matches())
		{
			reservedWord=true; //checks if link has player word in link
		}
		
		if(comment.matcher(baseurl).matches())
		{
			reservedWord=true;  //checks if link has comment word in link (added this for Reddit)
		}
	    
	    	   
	}
	
	/**
	 * Method that will implement CoreEx. Each html file will be parsed for the following
	 * features: the HTML tag of the main content node, the HTML tag(s) with the highest 
	 * frequency counts in the set S, an integer that represents the main content node’s score 
	 * @param file opens html file for parsing using Jsoup 
	 * @param baseurl the root url
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void CorexEx(File file, String baseurl) throws IOException
	{
		Document doc = Jsoup.parse(file, "UTF-8", baseurl);
		Element htmlbody = doc.body(); 
		System.out.println(htmlbody.outerHtml());
	}
	


	

}
