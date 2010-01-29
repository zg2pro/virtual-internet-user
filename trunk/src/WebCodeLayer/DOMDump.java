package WebCodeLayer;

import java.io.PrintStream;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe permettant d afficher le contenu d un document DOM
 * @author Yoann Janszen
 *
 */
public class DOMDump {
	
	/** 
	 * Prints the specified node, recursively. 
	 * */
	  public static void printDOMTree(Node node, PrintStream out) {
	    int type = node.getNodeType();
	    switch (type){
	      // print the document element
	      case Node.DOCUMENT_NODE: 
	        {
	          printDOMTree(((Document)node).getDocumentElement(), out);
	          break;
	        }

	        // print element with attributes
	      case Node.ELEMENT_NODE: 
	        {
	          out.print("<");
	          out.print(node.getNodeName());
	          NamedNodeMap attrs = node.getAttributes();
	          for (int i = 0; i < attrs.getLength(); i++)
	          {
	            Node attr = attrs.item(i);
	            out.print(" " + attr.getNodeName() + 
	                      "=\"" + attr.getNodeValue() + 
	                      "\"");
	          }
	          out.println(">");

	          NodeList children = node.getChildNodes();
	          if (children != null)
	          {
	            int len = children.getLength();
	            for (int i = 0; i < len; i++)
	              printDOMTree(children.item(i), out);
	          }

	          break;
	        }

	        // handle entity reference nodes
	      case Node.ENTITY_REFERENCE_NODE: 
	        {
	          out.print("&");
	          out.print(node.getNodeName());
	          out.print(";");
	          break;
	        }

	        // print cdata sections
	      case Node.CDATA_SECTION_NODE: 
	        {
	          out.print("<![CDATA[");
	          out.print(node.getNodeValue());
	          out.println("]]>");
	          break;
	        }

	        // print text
	      case Node.TEXT_NODE: 
	        {
	          out.print(node.getNodeValue());
	          break;
	        }

	        // print processing instruction
	      case Node.PROCESSING_INSTRUCTION_NODE: 
	        {
	          out.print("<?");
	          out.print(node.getNodeName());
	          String data = node.getNodeValue();
	          {
	            out.print(" ");
	            out.print(data);
	          }
	          out.println("?>");
	          break;
	        }
	    }

	    if (type == Node.ELEMENT_NODE)
	    {
	      out.print("</");
	      out.print(node.getNodeName());
	      out.print('>');
	    }
	  } // printDOMTree(Node, PrintWriter)
	  
	  
	  public static String xmlToString(Node node) {
	        try {
	            Source source = new DOMSource(node);
	            StringWriter stringWriter = new StringWriter();
	            Result result = new StreamResult(stringWriter);
	            TransformerFactory factory = TransformerFactory.newInstance();
	            Transformer transformer = factory.newTransformer();
	            transformer.transform(source, result);
	            return stringWriter.getBuffer().toString();
	        } catch (TransformerConfigurationException e) {
	            e.printStackTrace();
	        } catch (TransformerException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

}
