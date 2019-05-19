package sr_research;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.codec.binary.Base64;

//import org.junit.Test;



public class LinkTargetIdentificationURLParser {

	public static void main(String[] args) throws Exception 
	{
		File folder = new File("/Users/adam/eclipse-workspace/sr_research/sr_research_498_adam_new_project/going_headless/data/"); 
		File [] listOfFiles = folder.listFiles(); 
		for(File file: listOfFiles)
		{
			if(file.isFile())
			{
				byte[] decoded = Base64.decodeBase64(file.getName());
				String root_url= new String(decoded, "UTF-8");
				LinkAnalysis(file, root_url); 
			}
		}
 

	}
	
	
	
	public static void LinkAnalysis(File file, String baseurl) throws Exception 
	{
		Document doc = Jsoup.parse(file, "UTF-8", baseurl);
		Element link = doc.select("a").first();
		String absHref = link.attr("abs:href"); 
		System.out.println(absHref);
		
	       
	    
	}

}
