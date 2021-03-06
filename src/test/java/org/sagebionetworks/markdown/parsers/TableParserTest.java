package org.sagebionetworks.markdown.parsers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sagebionetworks.markdown.SynapseMarkdownProcessor;
import org.sagebionetworks.markdown.constants.WidgetConstants;

public class TableParserTest {
	SynapseMarkdownProcessor processor;
	TableParser parser;
	
	List<MarkdownElementParser> simpleParsers;
	
	@Before
	public void setup(){
		parser = new TableParser();
		
		simpleParsers = new ArrayList<MarkdownElementParser>();
		simpleParsers.add(new BoldParser());
		simpleParsers.add(new UnderscoreParser());
		simpleParsers.add(new SynapseMarkdownWidgetParser());
		
		parser.reset(simpleParsers);
	}
	
	@Test
	public void testExampleTable(){
		String start = "{| class=&quot;border&quot;";
		String exampleLine1 = "Row 1 Content Cell 1 | Row 1 Content Cell 2  | Row 1 Content Cell 3";
		String exampleLine2 = "Row 2 Content Cell 1  | Row 2 Content Cell 2  | Row 2 Content Cell 3";
		String end = "|}";
		StringBuilder tableOutput = new StringBuilder();
		MarkdownElements elements = new MarkdownElements(start);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements(exampleLine1);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements(exampleLine2);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements(end);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements("");
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		//check for a few items
		String html = tableOutput.toString();
		assertTrue(html.contains("<table id=\""+WidgetConstants.MARKDOWN_TABLE_ID_PREFIX+"0"));
		assertTrue(html.contains("class=\"tablesorter markdowntable border\""));
		assertTrue(html.contains("<tr><td>Row 1 Content Cell 1 </td>"));
		assertTrue(html.contains("</table>"));
	}
	
	@Test
	public void testTableWithTwoCol() {
		String exampleLine1 = "Row 1 Content Cell 1 | Row 1 Content Cell 2";
		StringBuilder tableOutput = new StringBuilder();
		MarkdownElements elements = new MarkdownElements(exampleLine1);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements("");
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		String html = tableOutput.toString();
		assertTrue(html.contains("<tr><td>Row 1 Content Cell 1 </td><td> Row 1 Content Cell 2</td></tr>"));
	}
	
	@Test
	public void testTableWithHeader() {
		String exampleLine1 = "Row 1 Content Cell 1 | Row 1 Content Cell 2  | Row 1 Content Cell 3";
		String exampleLine2 = "--: | -- | :--";
		StringBuilder tableOutput = new StringBuilder();
		MarkdownElements elements = new MarkdownElements(exampleLine1);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements(exampleLine2);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements("");
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		//check for a few items
		String html = tableOutput.toString();
		assertTrue(html.contains("<tr><th>Row 1 Content Cell 1 </th>"));
		assertFalse(html.contains("--"));
	}
	
	@Test
	public void testTableWithDifferentHeader() {
		String exampleLine1 = "| Row 1 Content Cell 1 | Row 1 Content Cell 2  | Row 1 Content Cell 3 |";
		String exampleLine2 = "| --: | -- | :-- |";
		StringBuilder tableOutput = new StringBuilder();
		MarkdownElements elements = new MarkdownElements(exampleLine1);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements(exampleLine2);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements("");
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		//check for a few items
		String html = tableOutput.toString();
		assertTrue(html.contains("<tr><th> Row 1 Content Cell 1 </th>"));
		assertFalse(html.contains("--"));
	}
	
	@Test
	public void testTableWithOneLine() throws IOException {
		String exampleLine1 = "**Row 1 Content Cell 1** | Row\\_Content\\_2  | Row 1 Content Cell 3";
		StringBuilder tableOutput = new StringBuilder();
		MarkdownElements elements = new MarkdownElements(exampleLine1);
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		
		elements = new MarkdownElements("");
		parser.processLine(elements);
		tableOutput.append(elements.getHtml());
		String html = tableOutput.toString();
		assertTrue(html.contains("<td><strong>Row 1 Content Cell 1</strong> </td>"));
		assertTrue(html.contains("Row&#95;Content&#95;2"));
	}
}
