package sr_research;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.codec.binary.Base64;

//import org.junit.Test;



public class InformationExtrationAlgorithms {

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
		File folder = new File("/Users/adam/eclipse-workspace/sr_research/sr_research_498_adam_new_project/going_headless/data/"); 
		File [] listOfFiles = folder.listFiles(); 
		for(File file: listOfFiles)
		{
			if(file.isFile())
			{
				byte[] decoded = Base64.decodeBase64(file.getName());
				String root_url= new String(decoded, "UTF-8");
				LinkTargetIdentification(root_url);
//				CorexEx(file, root_url); 
			}
		}
		
 

	}
	
	/**
	 * Method that will implement link target identification. Each root url
	 * will be parsed based on the following features: does the root link contain a number/Id 
	 * in the name of the link, 
	 * does the root link contain a date, does the root link contain a reserved word, 
	 * does the root link  end with a forward slash mark, 
	 * the length of the root link, and the number of forward slashes contained in the root link.  
	 * @param baseurl the root url that will be parsed for information
	 * @throws IOException exception thrown in case an i/o exception occurs
	 */
	public static void LinkTargetIdentification(String baseurl) throws IOException 
	{
		System.out.println(baseurl);
		   
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
