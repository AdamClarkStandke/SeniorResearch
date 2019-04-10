package sr_research;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Iterator;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;

public class Tool_testing {

	public static void main(String[] args) throws Exception 
	{
		getElements(); 

	}
	
	
	
	public static void getElements() throws Exception {
	    try (final WebClient webClient = new WebClient()) {
	        HtmlPage page = webClient.getPage("https://www.cnn.com");
	        HtmlElement x = page.getDocumentElement();
	        int value = x.getChildElementCount(); 
	        System.out.println(value + "");
	       
	    }
	}

}
