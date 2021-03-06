package org.sagebionetworks.markdown.parsers;

import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.sagebionetworks.markdown.utils.ServerMarkdownUtils;

public class ImageParserTest {
	ImageParser parser;
	
	@Before
	public void setup(){
		parser = new ImageParser();
		parser.reset(null);
	}
	
	@Test
	public void test(){
		String url = "http://test.com/a.png";
		String altText = "An Image";
		String line = "![" + altText+ "](" + url + ")";
		MarkdownElements elements = new MarkdownElements(line);
		parser.processLine(elements);
		String result = elements.getHtml();
		assertTrue(!result.contains("http://test.com/a.png"));
		assertTrue(result.contains(ServerMarkdownUtils.START_CONTAINER));
		assertTrue(result.contains(ServerMarkdownUtils.END_CONTAINER));
		
		Document doc = Jsoup.parse(result);
		parser.completeParse(doc);
		assertTrue(doc.html().contains("http://test.com/a.png"));
		assertTrue(doc.html().contains("<img"));
	}
}
