package RBL.Bank.Demon;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class XMLParser {

	 public static String editXmlTag(String xmlString, String tagName, String newValue) throws Exception {
	        // Parse the XML string into a DOM Document
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource inputSource = new InputSource(new StringReader(xmlString));
	        Document doc = builder.parse(inputSource);

	        // Fetch the tag by name
	        NodeList nodeList = doc.getElementsByTagName(tagName);
	        if (nodeList.getLength() == 0) {
	            throw new Exception("Tag '" + tagName + "' not found in the XML.");
	        }

	        // Update the value of the tag
	        Node node = nodeList.item(0); // Assuming only one occurrence of the tag
	        node.setTextContent(newValue);

	        // Convert the updated Document back to a string
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        DOMSource source = new DOMSource(doc);
	        StringWriter writer = new StringWriter();
	        StreamResult result = new StreamResult(writer);
	        transformer.transform(source, result);

	        return writer.toString();
	    }
	
}
