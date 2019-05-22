package sr_research;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;

//import org.junit.Test;


/**
 * The main class that houses both information extraction algorithms, 
 * namely Link-Target Identification and CoreEx. This main class can 
 * run either or the two extraction algorithms. When done the class will 
 * generate a csv file that will be inputed to the C4.5 machine learning 
 * algorithm. 
 * @author adam
 *
 */
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
    private static Pattern numpattern;
    private static Pattern datepattern;
    private static ArrayList<Storage> domNodes; 
    
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
		 numpattern = Pattern.compile(numberRegrex);
		 datepattern= Pattern.compile(dateRegrex);
		 domNodes = new ArrayList<Storage>(); 
	}

	/**
	 * Main method that opens the file directory where 
	 * each HTML file is stored(ie., the data directory).
	 * After opening the directory, each filename is decoded from
	 * base 64 to its URL name. Then each information extraction
	 * algorithm is called for further processing. Namely, Link 
	 * Target Identification and CoreEx.   
	 * @param args optional command line arguments
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void main(String[] args) throws IOException 
	{
		InformationExtrationAlgorithms info = new InformationExtrationAlgorithms(); 
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
				LinkTargetIdentification(root_url);
//				CorexEx(file, root_url, info); 
			}
		}
		
 

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
		
		if(comment.matcher(baseurl).find())
		{
			reservedWord=true;  //checks if link has comment word in link (added this for Reddit)
		}
	    
	    	   
	}
	
	/**
	 * Method that will implement CoreEx. Each HTML file will be parsed for the following
	 * features: the HTML tag of the main content node, the HTML tag with the highest 
	 * frequency counts in the set S, an integer that represents the main content node’s score 
	 * @param file opens HTML file for parsing using Jsoup Library 
	 * @param baseurl is the root URL of the web page
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void CorexEx(File file, String baseurl, InformationExtrationAlgorithms info) throws IOException
	{
		//Opens the document for traversing the body of the document
		Document doc = Jsoup.parse(file, "UTF-8", baseurl);
		Element htmlbody = doc.body();
		
		//gets the total amount of text contained in the body, used later for scoring
		String totalText = htmlbody.text();
		StringTokenizer tokens = new StringTokenizer(totalText);
		int pageText = tokens.countTokens(); 
		
		//calls the function that recursively traverses the dom tree
		nonTerminalNode(htmlbody, true, info);
		
		//calls the scoring function and determines the main content node
		Storage mainContent =  scoring(domNodes, pageText);
		
		//store the main content node's tag
		Element tag = mainContent.nonTerminalChild; 
		String mainTag = tag.id();
		
		//have to add the other features here used for project 
		
		
	}
	
	/**
	 * The recursive algorithm that implements CoreEx's determination of the text-to-link ratio for each
	 * DOM node in the tree. After doing so, each non-terminal node will have an associated text count, 
	 * link count, and a list of nodes that make up the main content of the web page.
	 * @param htmlbody is Element from the HTML page that represents a given node in the DOM tree
	 * @param flag is used to determine whether the textCnt or LinkCnt should be returned from the base case
	 * @param info is the main class used to instantiate the inner class Storage to store each node's attributes
	 * @return an integer that represents either terminal counts of text and links or the value zero which means
	 * the algorithm has finished
	 */
	public static int nonTerminalNode(Element htmlbody, boolean flag, InformationExtrationAlgorithms info)
	{
		//base case where node is a terminal node (ie., has no children)
		if(htmlbody.childNodeSize()==0)
		{
			int terminalTextCnt=0; 
			int terminalLinkCnt=0; 
			//checks to see if node's parent is a link
			Element parent = htmlbody.parent(); 
			if(parent.getElementById("a") != null)
			{
				if(flag==true) //if so the amount of text will be 1
				{
					terminalTextCnt=1;
					return terminalTextCnt; 
				}
				if(flag==false) //if so the amount of links will be 1
				{
					terminalLinkCnt=1;
					return terminalLinkCnt; 
				}
 
			}
			//checks to see node is textual node 
			else if (htmlbody.hasText())
			{
				String text_node = htmlbody.ownText(); 
				StringTokenizer tokens = new StringTokenizer(text_node);
				if(tokens != null)
				{
					if(flag==true) //if so the amount of text will be equal to how much text the node contains
					{
						terminalTextCnt = tokens.countTokens();
						return terminalTextCnt; 
					}
					if(flag==false) //since its parent is not a link node the link amount will be zero
					{
						terminalLinkCnt = 0;
						return terminalLinkCnt; 
					}
						
				}
			}
			//in all other cases the amount of text and links will be zero
			else
			{
				if(flag==true)
				{
					terminalTextCnt = 0; 
					return terminalTextCnt; 
				}
				if(flag==false)
				{
					terminalLinkCnt = 0;
					return terminalLinkCnt; 
				}
				 
			}
		}
		
		float childRatio=0; 
		int textCnt =0; 
		int linkCnt=0; 
		ArrayList<Element> S = new ArrayList<Element>();
		int setTextCnt=0; 
		int setLinkCnt=0;
		Elements children = htmlbody.children(); 
		for(Element child: children)
		{
			textCnt = textCnt + nonTerminalNode(child, true, info);
			linkCnt = linkCnt + nonTerminalNode(child, false, info); 
			childRatio = (((float)textCnt-linkCnt)/textCnt);
			if(childRatio>0.9f)
			{
				S.add(child); 
				setTextCnt = setTextCnt + textCnt; 
				setLinkCnt = setLinkCnt + linkCnt; 
			}
			Storage store = info.new Storage(child, S, textCnt, linkCnt, setTextCnt, setLinkCnt); 
			domNodes.add(store); 	
		}
		
		return 0; 
	}
	
	/**
	 * Inner-class used to store the values of a DOM node throughout CoreEx's execution
	 * @author Adam Standke
	 *
	 */
	public class Storage 
	{
		public Element nonTerminalChild; 
		public ArrayList<Element> S;
		public int textCnt; 
		public int linkCnt; 
		public int SetTextCnt; 
		public int SetLinkCnt;
		public double score; 
		
		public Storage(Element child, ArrayList<Element> set, int text, int link, int totaltext, int totallinks)
		{
			nonTerminalChild=child; 
			for (int i = 0 ; i<set.size();i++)
			{
			    S.add(set.get(i));
			}
			textCnt=text; 
			linkCnt=link; 
			SetTextCnt=totaltext; 
			SetLinkCnt=totallinks; 	
		}
		
		public void setScore(double score)
		{
			this.score=score; 
		}
	}
	
	/**
	 * CoreEx's scoring function that determines the main content node. CoreEx's weighted function
	 * outputs the node with the highest text-to-link ratio and this node along with its accumulated 
	 * values will be used as features for the C4.5 machine learning algorithm
	 * @param nodes all of the non-terminal nodes of the web page
	 * @param pageText the total amount text contained in the web page
	 * @return the node with the highest weighted score 
	 */
	public static Storage scoring(ArrayList<Storage> nodes, int pageText)
	{
		Storage highestScoringNode = null; 
		double max=0.0d; 
		float weightRatio = 0.99f; 
		float weightText = 0.01f; 
		Iterator<Storage> iterate = nodes.iterator();
		if(iterate.hasNext())
		{
			Storage node = iterate.next();
			double score = (weightRatio * (((float)node.SetTextCnt-node.SetLinkCnt)/node.SetTextCnt)) + 
					       (weightText * ((float)node.SetTextCnt/pageText)); 
			
			max=score;
			highestScoringNode=node;
			node.setScore(score);
		}
		while(iterate.hasNext())
		{
			Storage node = iterate.next();
			double score = (weightRatio * (((float)node.SetTextCnt-node.SetLinkCnt)/node.SetTextCnt)) + 
					       (weightText * ((float)node.SetTextCnt/pageText)); 
			
			if(score > max)
			{
				max=score;
				highestScoringNode=node;
				node.setScore(score);
			}
//			if (score == max)
//			{
//				figure out way to find higher nodes in dom tree
//			}
			
		}
	
		return highestScoringNode;
		
	}
	


	

}
