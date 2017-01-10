import javax.swing.DefaultListModel;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jcp.xml.dsig.internal.dom.DOMTransform;
import org.w3c.dom.*;

import sun.invoke.empty.Empty;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.util.Vector;
import java.util.logging.Logger;

public class XmlUtils {
	static Logger logger;

	public XmlUtils(Logger lgr) {
		logger = lgr;
	}
	public String convertToString(Document doc) throws Exception {
		
		StringWriter str = new StringWriter();
        TransformerFactory tranFactory = TransformerFactory.newInstance();
        Transformer transformer = tranFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult dest = new StreamResult(str);
        transformer.transform(source, dest);
        logger.info(str.toString());
        return str.toString();
	}
	public Document addChildNode(Document doc, String tag, String value)
	{
		
		return doc;
	}
	public Document getRequest(String srcFileName) throws Exception {
		
			File input = new File(srcFileName);
			if(!input.exists())
				input.createNewFile();
			/* DOM Parser */
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(input);
	        return doc;
	}
	
	public Document convertToXML(String data) throws Exception {
		
		/* DOM Parser */
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        ByteArrayInputStream str = new ByteArrayInputStream(data.getBytes("UTF-8"));
        Document doc = dBuilder.parse(str);
        return doc;
	}
	
	public Document createDocFromFactory() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.newDocument();
	}
	public void updateRequest(Document doc, String destFilePath) throws Exception {
		  File ouput = new File(destFilePath);
		  if(!ouput.exists())
			ouput.createNewFile();
	      TransformerFactory tranFactory = TransformerFactory.newInstance();
	      Transformer transformer = tranFactory.newTransformer();
	      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
	      DOMSource source = new DOMSource(doc);
	      StreamResult dest = new StreamResult(ouput);
	      transformer.transform(source, dest);
	}
	
	public Document saveXmlData(String data, String destFileName) throws Exception {
		File ouput = new File(destFileName);
		/* DOM Parser */
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        ByteArrayInputStream str = new ByteArrayInputStream(data.getBytes("UTF-8"));
        
        logger.info("size: " + data.length());
        
        Document doc = dBuilder.parse(str);
       //Document doc = dBuilder.parse(data);
        
        TransformerFactory tranFactory = TransformerFactory.newInstance();
        Transformer transformer = tranFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        DOMSource source = new DOMSource(doc);
        
        StreamResult dest = new StreamResult(ouput);
        transformer.transform(source, dest);
        
        return doc;
	}
	
	public String saveXmlLogData(String data, PrintWriter out) throws Exception {
		/* DOM Parser */
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        ByteArrayInputStream str = new ByteArrayInputStream(data.getBytes("UTF-8"));
        
        logger.info("size: " + data.length());
        
        Document doc = dBuilder.parse(str);
       //Document doc = dBuilder.parse(data);
        
        TransformerFactory tranFactory = TransformerFactory.newInstance();
        Transformer transformer = tranFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        DOMSource source = new DOMSource(doc);
		StringWriter str2 = new StringWriter();
        StreamResult dest = new StreamResult(str2);
        
        transformer.transform(source, dest);
        out.println(str2.toString());
        return str2.toString();
	}
	
	public boolean validateXml(String data) {
		
		try{
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        ByteArrayInputStream str = new ByteArrayInputStream(data.getBytes("UTF-8"));
	        dBuilder.parse(str);
	        logger.info("Valid XML Input");
	        return true;
		}
		catch(Exception e){
			logger.info("Invalid XML Input");
			e.printStackTrace();
			return false;
		}
	}
	
	public Document appendSecurityToRequest (String fileName, String counter, String macStr) throws Exception {
		
		Document docRequest = this.getRequest(fileName);
		Document docSecurity = this.getRequest("res/init/security.xml");
		
		// copy MAC_LABEL
		Node newNode = null;
		newNode = docRequest.createElement("MAC_LABEL");
		newNode.setTextContent(docSecurity.getElementsByTagName("MAC_LABEL").item(0).getTextContent());
		docRequest.getDocumentElement().appendChild(newNode);
		
//		Node macLbl = docRequest.getElementsByTagName("MAC_LABEL").item(0);
//		macLbl.setTextContent(docSecurity.getElementsByTagName("MAC_LABEL").item(0).getTextContent());
		
		// copy COUNTER
		newNode = null;
		newNode = docRequest.createElement("COUNTER");
		newNode.setTextContent(counter);
		docRequest.getDocumentElement().appendChild(newNode);
		
//		Node counterTag = docRequest.getElementsByTagName("COUNTER").item(0);
//		counterTag.setTextContent(counter);
		
		// generate HMAC & copy
		newNode = null;
		newNode = docRequest.createElement("MAC");
		newNode.setTextContent(macStr);
		docRequest.getDocumentElement().appendChild(newNode);
		
//		Node macNode = docRequest.getElementsByTagName("MAC").item(0);
//		macNode.setTextContent(macStr);
		return docRequest;
	}
	
	public Vector<Vector<String> > parseTemplate(String template) throws Exception {
		Vector<Vector<String> > data = new Vector<Vector<String>>();
		
		String fileName = "res/templates/" + template + "/tmplt_" + template + ".xml";
		logger.info(fileName);
		Document docTemplate = this.getRequest(fileName);
		this.convertToString(docTemplate);

		Element root = docTemplate.getDocumentElement();
		int totalCmdCount = Integer.valueOf(docTemplate.getDocumentElement().getAttribute("count"));

		logger.info("List Size " + totalCmdCount);
		NodeList cmdNameList = root.getElementsByTagName("NAME");
		NodeList cmdCountList = root.getElementsByTagName("COUNTER");
		for(int index = 0; index < cmdNameList.getLength(); index++)
		{
			if(cmdNameList.item(index) instanceof Element == false)
			{
				continue;
			}
			Vector<String> row = new Vector<String>();		
			logger.info("Node " + cmdNameList.item(index) + " KEY " + cmdNameList.item(index).getTextContent());
			logger.info("Node " + cmdCountList.item(index) + " KEY " + cmdCountList.item(index).getTextContent());
			row.addElement(cmdNameList.item(index).getTextContent());
			row.addElement(cmdCountList.item(index).getTextContent());
			data.addElement(row);
		}
			
			//cmdList.add(index, nodeList.item(index).getTextContent());
		return data;
	}
	
	public byte[] getAESKey() throws Exception {
		
		Document docSecurity = this.getRequest("res/init/security.xml");
		Node aesKey = docSecurity.getElementsByTagName("AES_KEY").item(0);
		return Base64.decode(aesKey.getTextContent().getBytes("UTF-8"));
	}
	

	public void saveRequest2(String data, String destFileName) throws Exception {
		
		File ouput = new File(destFileName);
		/* DOM Parser */
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        //Document doc = dBuilder.parse(ouput);
        
        ByteArrayInputStream str = new ByteArrayInputStream(new String("<student><firstname><name>mukesh</name></firstname></student>").getBytes("UTF-8"));
        Document doc = dBuilder.parse(str);
        
        // Output to console for testing    
       logger.info("####################################");
       TransformerFactory tranFactory = TransformerFactory.newInstance();
       Transformer transformer = tranFactory.newTransformer();
       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
       transformer.setOutputProperty(OutputKeys.METHOD, "xml");
       transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
       DOMSource source = new DOMSource(doc);
       //StreamResult dest = new StreamResult(System.out);
       StreamResult dest = new StreamResult(ouput);
      transformer.transform(source, dest);
	}

}
