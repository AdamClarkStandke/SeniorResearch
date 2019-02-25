package sr_research;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Tool_testing {

	public static void main(String[] args) throws IOException 
	{
		File input = new File("/Users/adam/Desktop/article_one.html");
		Document doc = Jsoup.parse(input, "UTF-8", "");
		System.out.println(doc.html());

	}

}
