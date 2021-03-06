package org.sagebionetworks.markdown.parsers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RowColumnParserTest {
	RowColumnParser parser;
	
	@Before
	public void setup(){
		parser = new RowColumnParser();
	}
	
	@Test
	public void testRowColumnHappyCase(){
		String line1 ="{row}";
		String line2 = "{column width=5 offset=1}Some content{column}{column width=6}More content{column}";
		String line3 = "{row}";
		MarkdownElements elements = new MarkdownElements(line1);
		parser.processLine(elements);
		assertTrue(elements.getHtml().contains("<div class=\"row\""));
		assertTrue(parser.isInMarkdownElement());
		elements = new MarkdownElements(line2);
		parser.processLine(elements);
		assertTrue(elements.getHtml().contains("col-sm-6"));
		assertTrue(elements.getHtml().contains("col-sm-5"));
		assertTrue(elements.getHtml().contains("col-sm-offset-1"));
		
		elements = new MarkdownElements(line3);
		parser.processLine(elements);
		assertTrue(elements.getHtml().contains("</div>"));
		assertFalse(parser.isInMarkdownElement());
	}
	
	@Test
	public void testRowColumn(){
		String line = "{column offset = 1 width = 2}";
		MarkdownElements elements = new MarkdownElements(line);
		parser.processLine(elements);
		assertTrue(elements.getHtml().contains("col-sm-2"));
		assertTrue(elements.getHtml().contains("col-sm-offset-1"));
	}
}
