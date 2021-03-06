package org.sagebionetworks.markdown.parsers;

import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.sagebionetworks.markdown.utils.ServerMarkdownUtils;

public class CodeSpanParserTest {
	CodeSpanParser parser;
	
	@Before
	public void setup(){
		parser = new CodeSpanParser();
		parser.reset(null);
	}
	
	@Test
	public void testCodeSpan(){
		String text = "a basic `code span` test";
		MarkdownElements elements = new MarkdownElements(text);
		parser.processLine(elements);
		String result = elements.getHtml();
		assertTrue(!result.contains("<code>code span</code>"));
		assertTrue(result.contains(ServerMarkdownUtils.START_CONTAINER));
		assertTrue(result.contains(ServerMarkdownUtils.END_CONTAINER));
		
		Document doc = Jsoup.parse(result);
		parser.completeParse(doc);
		assertTrue(doc.html().contains("<code>code span</code>"));
	}
}
