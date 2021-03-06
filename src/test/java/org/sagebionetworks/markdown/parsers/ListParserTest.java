package org.sagebionetworks.markdown.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.sagebionetworks.markdown.utils.ServerMarkdownUtils;

public class ListParserTest {
	
	ListParser parser;
	CodeParser codeParser;
	HeadingParser headParser;
	
	@Before
	public void setup(){
		parser = new ListParser();
		parser.reset(null);
		
		codeParser = new CodeParser();
		codeParser.reset(null);
		
		headParser = new HeadingParser();
		headParser.reset(null);
	}
	
	@Test
	public void testOrderedList() {
		String example = 
			"1. First item\n" +
			"2. Second item\n";
		String[] lines = example.split("\n");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			MarkdownElements elements = new MarkdownElements(lines[i]);
			parser.processLine(elements);
			result.append(elements.getHtml());
		}
		//process a final empty line (just like the processor)
		MarkdownElements elements = new MarkdownElements("");
		parser.processLine(elements);
		result.append(elements.getHtml());
		assertEquals("<ol start=\"1\"><li><p>First item</p></li><li><p>Second item</p></li></ol>", result.toString());
		
		String nestedExample = 
			"1. List item one\n" +
			"    1. bunk\n" +
			"    2. bupkis\n" +
			"2. Second one\n";	
		String[] lines2 = nestedExample.split("\n");
		StringBuilder result2 = new StringBuilder();
		for (int i = 0; i < lines2.length; i++) {
			MarkdownElements elements2 = new MarkdownElements(lines2[i]);
			parser.processLine(elements2);
			result2.append(elements2.getHtml());
		}
		//process a final empty line (just like the processor)
		MarkdownElements extraLine = new MarkdownElements("");
		parser.processLine(extraLine);
		result2.append(extraLine.getHtml());
		assertEquals("<ol start=\"1\"><li><p>List item one</p><ol start=\"1\"><li><p>bunk</p></li><li><p>bupkis</p></li></ol></li><li><p>Second one</p></li></ol>", result2.toString());
	}

	@Test
	public void testMixedNestedLists(){
		String example = 
				"*   Abacus\n"+
				"    * answer\n"+
				"*   Bubbles\n"+
				"    1.  bunk\n"+
				"    2.  bupkis\n"+
				"        * BELITTLER\n"+
				"    3. burper\n"+
				"*   Cunning\n";
		String[] lines = example.split("\n");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			MarkdownElements elements = new MarkdownElements(lines[i]);
			parser.processLine(elements);
			result.append(elements.getHtml());
		}
		//process a final empty line (just like the processor)
		MarkdownElements elements = new MarkdownElements("");
		parser.processLine(elements);
		result.append(elements.getHtml());
		assertEquals("<ul><li><p>Abacus</p><ul><li><p>answer</p></li></ul></li><li><p>Bubbles</p><ol start=\"1\"><li><p>bunk</p></li><li><p>bupkis</p><ul><li><p>BELITTLER</p></li></ul></li><li><p>burper</p></li></ol></li><li><p>Cunning</p></li></ul>", result.toString());
	}
	
	@Test
	public void testMultipleParagraphs() {
		String example = 
			"* First item\n" +
			"    This is another paragraph\n" +
			"    This is another paragraph\n";
		String[] lines = example.split("\n");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			MarkdownElements elements = new MarkdownElements(lines[i]);
			codeParser.processLine(elements);
			parser.processLine(elements);
			result.append(elements.getHtml());
		}
		//process a final empty line (just like the processor)
		MarkdownElements elements = new MarkdownElements("");
		parser.processLine(elements);
		result.append(elements.getHtml());
		assertEquals("<ul><li><p>First item</p><p>This is another paragraph</p><p>This is another paragraph</p></li></ul>", result.toString());
	}
	
	@Test
	public void testListWithCode() {
		String example = 
			"* First item\n" +
			"    ```\n" +
			"    sudo apt-get install git\n" +
			"    sudo apt-get install curl\n" +
			"    sudo apt-get install python python-setuptools python-pip\n" +
			"    sudo pip install python-magic\n" +
			"    ```\n";
		String[] lines = example.split("\n");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			MarkdownElements elements = new MarkdownElements(lines[i]);
			parser.processLine(elements);
			codeParser.processLine(elements);
			result.append(elements.getHtml());

		}
		//process a final empty line (just like the processor)
		MarkdownElements elements = new MarkdownElements("");
		parser.processLine(elements);
		result.append(elements.getHtml());
		assertTrue(result.toString().contains("<ul><li><p>First item</p><pre><code class=\""+ServerMarkdownUtils.DEFAULT_CODE_CSS_CLASS+"\">sudo apt-get install git"));
		assertTrue(result.toString().contains("sudo apt-get install curl"));
		assertTrue(result.toString().contains("sudo apt-get install python python-setuptools python-pip"));
		assertTrue(result.toString().contains("sudo pip install python-magic</code></pre></li></ul>"));
	}
	
	@Test
	public void testWithHeader() {
		String example = 
			"1. #### Heading1\n" +
			"    1. #### Heading2\n";
		String[] lines = example.split("\n");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			MarkdownElements elements = new MarkdownElements(lines[i]);
			parser.processLine(elements);
			headParser.processLine(elements);
			result.append(elements.getHtml());
		}
		//process a final empty line (just like the processor)
		MarkdownElements elements = new MarkdownElements("");
		parser.processLine(elements);
		result.append(elements.getHtml());
		assertEquals("<ol start=\"1\"><li><p><h4>Heading1</h4></p><ol start=\"1\"><li><p><h4>Heading2</h4></p></li></ol></li></ol>", result.toString());
	}
	
}
