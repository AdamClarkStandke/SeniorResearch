package linkTargetIdentification_JavaCode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVWriter;

import java.util.List;
import java.util.Scanner;
import java.util.Set; 

//import org.junit.Test;


/**
 * The main class that houses the information extraction system called 
 * Link-Target Identification. This main class runs the program  
 * and when done the class will  
 * generate a csv file that will be inputed to the C4.5 machine learning 
 * algorithm.
 * @author Adam Standke
 *
 */
public class LinkTargetId {

	//instance variables used for link-target identification and CoreEx
	private static String gallery_pattern = "\\bgallery?";
	private static String video_pattern = "\\bvideos?";
	private static String podcast_pattern = "\\bpodcasts?";
	private static String picture_pattern = "\\bpictures?"; 
    private static String image_pattern = "\\bimages?";
    private static String photo_pattern = "\\bphotos?";
    private static String slideshow_pattern = "\\bslideshows?";
    private static String episode_pattern = "\\bepisodes?";
    private static String player_pattern = "\\bplayers?";
    private static String id_pattern = "id=[\\d]+";
    private static String numberRegrex= "[-a-zA-Z]\\d{6,}[-/]?|[/]\\d{5,}[/]"; 
    private static String dateRegrex ="\\d{2,4}[-/]\\d{1,2}[-/]\\d{1,4}|[/]\\d{8}[-]";
    private static String classification=null; 
    private static Pattern gallery; 
    private static Pattern podcast; 
    private static Pattern picture; 
    private static Pattern video;
    private static Pattern image;
    private static Pattern photo;
    private static Pattern slideshow;
    private static Pattern episode;
    private static Pattern player;
    private static Pattern idpattern;
    private static Pattern numpattern;
    private static Pattern datepattern;
	private static int count;
	private static File Linkfile;  
	private static FileWriter linkOutputfile;
	private static CSVWriter linkwriter; 
	
    /**
     * Constructor that compiles the case-sensitive regrex pattern once, 
     * so that it does not have to be recompiled multiple times for each web page 
     * @throws IOException 
     */
	public LinkTargetId() throws IOException 
	{
		 gallery = Pattern.compile(gallery_pattern, Pattern.CASE_INSENSITIVE); 
		 video = Pattern.compile(video_pattern, Pattern.CASE_INSENSITIVE);
		 image = Pattern.compile(image_pattern, Pattern.CASE_INSENSITIVE);
		 photo = Pattern.compile(photo_pattern, Pattern.CASE_INSENSITIVE);
		 slideshow = Pattern.compile(slideshow_pattern, Pattern.CASE_INSENSITIVE);
		 episode = Pattern.compile(episode_pattern, Pattern.CASE_INSENSITIVE);
		 player = Pattern.compile(player_pattern, Pattern.CASE_INSENSITIVE);
		 picture = Pattern.compile(picture_pattern, Pattern.CASE_INSENSITIVE);
		 podcast = Pattern.compile(podcast_pattern, Pattern.CASE_INSENSITIVE);
		 idpattern = Pattern.compile(id_pattern, Pattern.CASE_INSENSITIVE);
		 numpattern = Pattern.compile(numberRegrex);
		 datepattern= Pattern.compile(dateRegrex);
		 count=0; 
		 Linkfile = new File("/Users/adam/Desktop/link_results_nonarticles_May272019_v2.csv");
		 linkOutputfile = new FileWriter(Linkfile);
		 linkwriter = new CSVWriter(linkOutputfile);
		 
	}

	/**
	 * Main method that opens the file directory where 
	 * each HTML file is stored(ie., the data directory).
	 * After opening the directory, each filename is decoded from
	 * base 64 to its URL name. Then the Link 
	 * Target Identification program is called   
	 * @param args optional command line arguments
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void main(String[] args) throws IOException 
	{
		//simple input scanner in which user types in the classification of data and where
		//data is located in the file directory
		Scanner scan = new Scanner(System.in);
		System.out.print("Are they articles:");
		classification = scan.nextLine();
		System.out.println("Please Enter Directory");
		String directory = scan.nextLine(); 
		scan.close();
		
		new LinkTargetId(); 
		
		//writes the header of features for link-target Id csv
		String[] linkheader = {"Number", "BaseUrl","Number", "Date", "Length", "EndingSlash", "ReservedWord", "SlashCount", "Article"}; 
		linkwriter.writeNext(linkheader);
		
		
		//prints output to files  
		PrintStream fileOut = new PrintStream("linktarget.html");
		System.setOut(fileOut);
		
		
		//opens file directory, pulls in files, decodes filename, and then run's info extract algorithm
		List<Path> files = Files.walk(Paths.get(directory)).collect(Collectors.toList());
		Iterator<Path> path = files.iterator();
		while(path.hasNext())
		{
			Path path2 = path.next(); 
			File file = path2.toFile();
			if (file.isFile() && (!(file.isHidden())))
			{
				count++; 
				String rm_html = file.getName(); 
				rm_html=rm_html.replace(".html", ""); 
				byte[] decoded = Base64.decodeBase64(rm_html);
				String root_url= new String(decoded, "UTF-8");
				LinkTargetIdentification(root_url);
				
			}
		 
		}
		linkwriter.close(); 
	}
	
	/**
	 * Method that will implement link target identification. Each root URL
	 * will be parsed based on the following features: does the root link contain a number or Id 
	 * in the name of the link, 
	 * does the root link contain a date, does the root link contain a reserved word, 
	 * does the root link  end with a forward slash mark, 
	 * the length of the root link, and the number of forward slashes contained in the root link.  
	 * @param baseurl the root URL that will be parsed for information
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
		
		//pre-processes url to get rid of the  first terms up to .com 
		StringBuffer https = new StringBuffer("https://"); 
		StringBuffer http  = new StringBuffer("http://"); 
		if(baseurl.contains(https) || baseurl.contains(http))
		{
			baseurl=baseurl.replaceFirst("([\\w.:/]+com\\/)", ""); 
		}
		
		System.out.println(baseurl);
		//checks to see if link ends with slash
		if(baseurl.endsWith("/"))
		{
			endWithSlash=true; 
		}
		
		//computes the approximate length of the link 
		lenghLink=baseurl.length(); 
		
		//checks to see if date is in the format like /mdy/-mdy/-mdy/ where m=month,d=day, y=year
		if (datepattern.matcher(baseurl).find())
		{
			date=true; 
		}
		
		//counts the number of forwards slash marks in a link
		numForwardSlashes=StringUtils.countMatches(baseurl, "/"); 
		
		//checks to see if link contains a Id in the name link 
		if(idpattern.matcher(baseurl).find())
		{
			num_or_id=true; 
		}
		
		//checks to see if link contains a contiguous number in the name link
	    if (numpattern.matcher(baseurl).find())  
		{
	    	num_or_id=true;  
	 
		}
	    
	    //will determine if link has a reserved word in its link as dictated by the literature
		if(gallery.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has gallery word in link
		}
		
		if(picture.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has picture word in link
		}
		
		if(podcast.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has podcast word in link
		}
		
		if(video.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has video word in link
		}
		
		if(image.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has image word in link
		}
		 
		if(photo.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has photo word in link
		}
		 
		if(slideshow.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has slide-show word in link
		}

		if(episode.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has episode word in link
		}
		 
		if(player.matcher(baseurl).find())
		{
			reservedWord=true; //checks if link has player word in link
		}
		
		//writing to csv file for link target the different feature values
		String number = Boolean.toString(num_or_id);
		String num = Integer.toString(count); 
		String Date = Boolean.toString(date); 
		String word = Boolean.toString(reservedWord);
		String slash = Boolean.toString(endWithSlash); 
		String Length = Integer.toString(lenghLink); 
		String numslashes = Integer.toString(numForwardSlashes); 
		String[] data = {num, baseurl, number, Date, Length, slash, word, numslashes, classification}; 
		linkwriter.writeNext(data);
  
	}
}
	

