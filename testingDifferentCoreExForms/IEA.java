package testingDifferentCoreExForms;

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
 * The main class that houses both information extraction algorithms, 
 * namely Link-Target Identification and CoreEx. This main class can 
 * run either of the two extraction algorithms. When done the class will 
 * generate a csv file that will be inputed to the C4.5 machine learning 
 * algorithm.(hopefully!!!) 
 * @author Adam Standke
 *
 */
public class IEA {

	//instance variables used for link-target identification and CoreEx
	private static String gallery_pattern = "\\bgallery?";
	private static String video_pattern = "\\bvideos?";
    private static String image_pattern = "\\bimages?";
    private static String photo_pattern = "\\bphotos?";
    private static String slideshow_pattern = "\\bslideshows?";
    private static String episode_pattern = "\\bepisodes?";
    private static String player_pattern = "\\bplayers?";
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
    private static Pattern idpattern;
    private static Pattern numpattern;
    private static Pattern datepattern;
    private static IEA info; 
    private static double max=0.0d; 
	private static float weightRatio = 0.99f; 
	private static float weightText = 0.01f; 
	private static int pageText;
	private static int count;
	private static Node htmlbody;
	private static File coreExfile;
	private static File Linkfile;  
	private static FileWriter linkOutputfile;
	private static FileWriter corexOutputfile;
	private static CSVWriter coreExwriter;
	private static CSVWriter linkwriter; 
	
    /**
     * Constructor that compiles the case-sensitive regrex pattern once, 
     * so that it does not have to be recompiled multiple times for each web page 
     * @throws IOException 
     */
	public IEA() throws IOException 
	{
		 gallery = Pattern.compile(gallery_pattern, Pattern.CASE_INSENSITIVE); 
		 video = Pattern.compile(video_pattern, Pattern.CASE_INSENSITIVE);
		 image = Pattern.compile(image_pattern, Pattern.CASE_INSENSITIVE);
		 photo = Pattern.compile(photo_pattern, Pattern.CASE_INSENSITIVE);
		 slideshow = Pattern.compile(slideshow_pattern, Pattern.CASE_INSENSITIVE);
		 episode = Pattern.compile(episode_pattern, Pattern.CASE_INSENSITIVE);
		 player = Pattern.compile(player_pattern, Pattern.CASE_INSENSITIVE);
		 idpattern = Pattern.compile(id_pattern, Pattern.CASE_INSENSITIVE);
		 numpattern = Pattern.compile(numberRegrex);
		 datepattern= Pattern.compile(dateRegrex);
		 pageText=0;
		 count=0; 
		 Linkfile = new File("/Users/adam/Desktop/resultslink_nonarticles_May262019.csv");
		 coreExfile = new File("/Users/adam/Desktop/resultscoreEx_nonarticles_May262019.csv");
		 linkOutputfile = new FileWriter(Linkfile);
		 corexOutputfile = new FileWriter(coreExfile);
		 linkwriter = new CSVWriter(linkOutputfile);
		 coreExwriter = new CSVWriter(corexOutputfile);
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
		//simple input scanner in which user types in the classification of data and where
		//data is located in the file directory
		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter classification");
		String classification = scan.nextLine();
		System.out.println("Please Enter Directory");
		String directory = scan.nextLine(); 
		scan.close();
		
		//Instantiates the main class and the inner storage class
		info = new IEA(); 
		
		//writes the header of features for link-target Id csv
		String[] linkheader = {"Number", "BaseURl","Number", "Date", "Length", "EndingSlash", "ReservedWord", "SlashCount"}; 
		linkwriter.writeNext(linkheader);
		
		//writes the header of features for CoreEx csv
		String[] coreExheader = {"Number","BaseUrl", "MainConentTag", "HighestFreqTagSetS", "FrequencyCount", "MainContentScore", "DepthMainContentinDOMTree"}; 
		coreExwriter.writeNext(coreExheader);
		
		//prints output to files called ModifiedCorex where only parentlinks are scored 
		PrintStream fileOut = new PrintStream("nonarticlelinks.html");
		System.setOut(fileOut);
		
		
		//opens file directorectory, pulls in files, decodes filename, and then run's info extract algorithms
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
				LinkTargetIdentification(root_url, classification);
				//CorexEx(file, root_url, classification);
			}
		 
		}
		linkwriter.close(); 
		coreExwriter.close();
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
	public static void LinkTargetIdentification(String baseurl, String classification) throws IOException 
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
	
	/**
	 * Method that will implement CoreEx. Each HTML file will be parsed for the following
	 * features: the HTML tag of the main content node, the HTML tag with the highest 
	 * frequency counts in the set S, an integer that represents the main content node’s score 
	 * @param file opens HTML file for parsing using Jsoup Library 
	 * @param baseurl is the root URL of the web page
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void CorexEx(File file, String baseurl, String classification) throws IOException
	{
		
		//Opens the document for traversing the document
		Document doc = Jsoup.parse(file, "UTF-8", baseurl);
		doc.outputSettings().indentAmount(0).prettyPrint(false);
		htmlbody = doc.root();
		
		//gets the total amount of text contained in the body, used later for scoring
		String totalText = ((Element) htmlbody).text();
		StringTokenizer tokens = new StringTokenizer(totalText);
		pageText = tokens.countTokens();

		//get the body node on which CoreEx will be run on
		Node body = doc.body();
		
		//the two storage mechanisms for storing information from CoreEx 
		int[] terminalValues = new int[2]; 
		Storage store = info.new Storage();	
		
		//calls the main function of CoreEX that recursively traverses the DOM tree
		nonTerminalNode(body, terminalValues, store);
	
		//<----Features extracted from CoreEx---->
		
		//gets the main content Tag of the web page 
		Node mainContentTag = store.getMainContentNode(); 
		String mainTagName = mainContentTag.nodeName();
		System.out.println(baseurl);
		System.out.println("\n\n");
		System.out.println("Main Content node:");
		System.out.println(mainTagName);
		List<Node> children = mainContentTag.childNodes();
		for(Node child: children)
		{
			System.out.println(child.outerHtml());
		}
		System.out.println("\n");
		
		//gets the score that CoreEx assigned to the main content of the web page
		double scoreMainContentNode = store.getScore();
		System.out.println("Main Content node's Score:");
		System.out.print(scoreMainContentNode);
		System.out.println("\n");
		
		//gets the node with the highest frequency count in the set S
		System.out.println("Sub Node with the highest frequency count:");
		Iterator<Set<Node>> tag = store.getS().iterator(); 
		ArrayList<String> S = new ArrayList<String>(); 
		while(tag.hasNext())
		{
			Set<Node> nodes = tag.next();
			Iterator<Node> nodeIterator = nodes.iterator();
			while (nodeIterator.hasNext())
			{
				Node mainContent = nodeIterator.next();
				S.add(mainContent.nodeName()); 
			}
		}
		//procedure to find the most frequent node in the set S
		int max_count = 1; 
	    int curr_count = 1;
	    String first = null; 
	    if (!(S.isEmpty()))
	    {
	    	first= S.get(0); 
	    }
	    for (int i = 1; i < S.size(); i++) 
        { 
            if (S.get(i).equalsIgnoreCase(S.get(i-1)))
                curr_count++; 
            else 
            { 
                if (curr_count > max_count) 
                { 
                    max_count = curr_count; 
                    first = S.get(i-1); 
                } 
                curr_count = 1; 
            } 
        } 
        // If last element is most frequent 
        if (curr_count > max_count) 
        { 
            max_count = curr_count; 
            first = S.get(S.size()-1); 
        } 
        
        System.out.println(first);
        System.out.println("\n");
        System.out.println("Frequcney count:");
        System.out.print(max_count);
        System.out.println("\n\n\n");
		
        //CSV file that contains feature values extracted by CorexEx
        String freqCount = Integer.toString(max_count);
        String num = Integer.toString(count); 
        String mainScore = Double.toString(scoreMainContentNode);
        String depthDomTree = Integer.toString(store.getNodeDepth()); 
        String[] data = {num, baseurl, mainTagName, first, freqCount, mainScore, depthDomTree, classification}; 
        coreExwriter.writeNext(data);
		
	}
	
	/**
	 * The recursive algorithm that implements CoreEx's determination of the text-to-link ratio for each
	 * DOM node in the tree. After doing so, each non-terminal node will have an associated text count, 
	 * link count, and a list of nodes that make up the main content of the web page.Furthermore
	 * the algorithm implements CoreEx's scoring function that determines the main content node. 
	 * CoreEx's weighted function outputs the node with the highest text-to-link ratio and 
	 * this node along with its accumulated values will be used as features for the C4.5 machine learning algorithm
	 * @param child2 is Element from the HTML page that represents a given node in the DOM tree
	 * @param terminalValues an array that stores the initial terminal values 
	 * @param store Storage class that saves the main content node, and other features
	 * @return an integer array that represents either terminal counts of text and links nodes used for 
	 * subsequent counting of setTextCnt and setLinkCnt
	 */
	public static int[] nonTerminalNode(Node child2, int[] terminalValues, Storage store)
	{
		//initializing base counts 
		terminalValues[0]=0; //holds the word count 
		terminalValues[1]=0; //holds the link count
		//base case where node's child is either a textnode, linknode or neither (ie. terminal node)
		if(child2.childNodeSize()==1 && child2.childNode(0) instanceof TextNode)
		{
			 
			//if so the amount of text will be equal to how much text the node contains
			Node terminalchild = child2.childNode(0);
			String text_node =  ((TextNode) terminalchild).getWholeText();  
			StringTokenizer tokens = new StringTokenizer(text_node);
			if(tokens != null)
			{
				terminalValues[0] = tokens.countTokens();
			}
			terminalValues[1] = 0;
			return terminalValues; 
				
		}
		else if(child2.childNodeSize()==1 && child2.childNode(0) instanceof Element)
		{
			//determines if node's parent is a linknode 
			Node terminalchild = child2.childNode(0);
			Node parent = terminalchild.parent();
			String parentName = parent.nodeName();  
			if(parentName.equals("a"))
			{
				terminalValues[0]=1; 
				terminalValues[1]=1; 
				return terminalValues; 
 
			}
		}
		else if (child2.childNodeSize()==0) //third case used as a catch all
		{
			terminalValues[0]=0; 
			terminalValues[1]=1; 
			return terminalValues; 
		}
		//main portion of CoreEx algorithm where non-terminal nodes are handled
		float childRatio=0; 
		int textCnt =0; 
		int linkCnt=0; 
		Set<Node> S = new LinkedHashSet<Node>(); //set is used to store nodes
		int setTextCnt=0; 
		int setLinkCnt=0;
		List<Node> children = child2.childNodes();
		for(Node child: children)  //for loop where each child node's text and link counts are iterated through
		{
			if (!(child instanceof TextNode)) //used to deal with white space characters found in the HTML document
			{
				int[] tempValues = nonTerminalNode(child, terminalValues, store); //recursive portion of CoreEx 
				textCnt = textCnt + tempValues[0];
				linkCnt = linkCnt + tempValues[1]; 
				childRatio = (((float)textCnt-linkCnt)/textCnt);
				if(childRatio>0.9f)
				{
					S.add(child); 
					setTextCnt = setTextCnt + textCnt; 
					setLinkCnt = setLinkCnt + linkCnt; 
				}
			}
		}
		//storage and scoring portion of CoreEx
		if (!(S.isEmpty()))   
		{
			Set<Set<Node>> setNodes = store.getS();
		    if(!(setNodes.contains(S))) //a check to make sure that current set is not already part of stored set
			{
				//CoreEx's weighted scoring function
				double score = (weightRatio * (((float)setTextCnt-setLinkCnt)/setTextCnt)) + 
					       	   (weightText * ((float)setTextCnt/pageText)); 
				
				//if storage is initially empty add the set S to storage and keep the values of 
				//setTextCnt, setLinkCnt, its score, and the depth within the DOM tree
				if (store.isEmpty())
				{
					store.setMainContentNode(child2);
					store.setS(S);
					store.setSetTextCnt(setTextCnt);
					store.setSetLinkCnt(setLinkCnt);
					store.setScore(score);
					store.setNodeDepth(getDepth(child2));
					max=score; 
				}
				else //else apply the the procedure for finding the highest scoring node
				{
					if(score > max)
					{
						store.remove();
						store.setMainContentNode(child2);
						store.setS(S);
						store.setSetTextCnt(setTextCnt);
						store.setSetLinkCnt(setLinkCnt);
						store.setScore(score);
						store.setNodeDepth(getDepth(child2));
						max=score; 
					}
					else if (score==max) //if the score are tied, the node higher in the DOM tree is stored
					{
						int previousDepth = store.getNodeDepth(); 
						int newDepth = getDepth(child2); 
						if (newDepth < previousDepth)
						{
							store.remove();
							store.setMainContentNode(child2);
							store.setS(S);
							store.setSetTextCnt(setTextCnt);
							store.setSetLinkCnt(setLinkCnt);
							store.setScore(score);
							store.setNodeDepth(newDepth);
							max=score; 
						}
						else if (newDepth > previousDepth)
						{
							; 
						}
						else //if the nodes have the same depth (ie., siblings) choose randomly 
						{
							int randomPick = (int)Math.random()*1; 
							if (randomPick==0) //if a zero is returned do nothing
							{
								; 
							}
							else //else if a 1 is chosen at random store the node
							{
								store.remove();
								store.setMainContentNode(child2);
								store.setS(S);
								store.setSetTextCnt(setTextCnt);
								store.setSetLinkCnt(setLinkCnt);
								store.setScore(score);
								store.setNodeDepth(newDepth);
								max=score; 
							}
						}
						
					}
				}
			
			}
		}
		return terminalValues; 
		
		
	}
	
	/**
	 * Inner-class used to store the highest scoring node and its respective attributes
	 * @author Adam Standke
	 *
	 */
	public class Storage 
	{
		public int  nodeDepth=0; 
		public Node mainNode; 
		public Set<Set<Node>> S= new LinkedHashSet<Set<Node>>(); 
		public int setLinkCnt=0; 
		public int setTextCnt=0; 
		public double score=0.0; 
		
		/**
		 * Class constructor that initializes the class 
		 */
		public Storage (){}
		
		/**
		 * method that checks to see if the set that stores the set of nodes is empty
		 * @return boolean that means either true or false
		 */
		public boolean isEmpty()
		{
			if(S.isEmpty())
			{
				return true; 
			}
			else
			{
				return false;
			}
			
		}
		
		public void setMainContentNode(Node mainContent)
		{
			this.mainNode=mainContent; 
		}
		
		public Node getMainContentNode()
		{
			return this.mainNode; 
		}
		
		/**
		 * Method that holds the score of the node
		 * @param score a double that represents its score, higher scores are better [0,1]
		 */
		public void setScore(double score)
		{
			this.score=score; 
		}

		/**
		 * Method that holds the depth of the node in the DOM tree
		 * @return an int that represent the depth of the node in the DOM tree
		 */
		public int getNodeDepth() {
			return nodeDepth;
		}
		
		/**
		* Method that stores the Node's depth in the DOM tree
		*/
		public void setNodeDepth(int nodeDepth) {
			this.nodeDepth = nodeDepth;
		}
		
		/**
		 * Returns the storage structure that holds the set of nodes S that holds the content
		 *
		 */
		public Set<Set<Node>> getS() {
			return S;
		}
		
		/**
		 * Adds to the storage structure a set of nodes S that holds the content
		 * @param s Set<Nodes> holds the set of nodes that holds the content
		 */
		public void setS(Set<Node> s) {
			S.add(s);
		}
		
		/**
		 * Clears the storage structure for another set of nodes to be stored
		 */
		public void remove()
		{
			if(!(this.isEmpty()))
			{
				S.clear();
			}
		}

		/**
		 * Keeps track of the node's setTextCnt
		 * @return an int that represents setTextCnt
		 */
		public int getSetTextCnt() {
			return setTextCnt;
		}
		
		/**
		 * Stores the node's setTextCnt 
		 * @param e an int that represents the node's setTextCnt
		 */
		public void setSetTextCnt(int e) {
			this.setTextCnt=e; 
		}
		
		/**
		 * Keeps track of the node's setLinkCnt
		 * @return an int that represents setLinkCnt
		 * 
		 */
		public int getSetLinkCnt() {
			return setLinkCnt;
		}

		/**
		 * Stores the node's setLinkCnt 
		 * @param e an int that represents the node's setLinkCnt
		 */
		public void setSetLinkCnt(int e) {
			this.setLinkCnt=e; 
		}
		
		/**
		 * Returns the score of the node computed by CoreEx function
		 * @return an int that represents a node's score
		 */
		public double getScore() {
			return score;
		}
		
	}
	
	/**
	 * Method that is used to calculate a node's depth within a 
	 * DOM tree by calculating the number of steps it takes to reach the root
	 * of the tree
	 * @param n a Node that is part of the DOM tree
	 * @return an int that represents a Node's depth in the DOM tree
	 */
	public static int getDepth(Node n)
	{
		int count=0; 
		while(n.hasParent() && !(n.equals(htmlbody)))
		{
			n=n.parent(); 
			count++; 
		}
		
		return count;
		
	}


	

}
