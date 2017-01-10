import java.io.File;
import java.util.Vector;

import javax.swing.DefaultListModel;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class Templates extends XmlUtils{
	
	DefaultListModel<String> tmpltList = new DefaultListModel<String>();
		
	public Templates() {
		super(logger);
		//load all the template name from "res/templates" directory
		File dir = new File("res/templates");
		File[] list = dir.listFiles();
		for(int i = 0; i < list.length && list[i].isDirectory(); i++)
			this.tmpltList.addElement(list[i].getName());
	}
	
	public DefaultListModel<String> getTmpltList() {
		return tmpltList;
	}

	public void setTmpltList(DefaultListModel<String> tmpltList) {
		this.tmpltList = tmpltList;
	}

	public String saveTemplate(String tmpltName, Vector<Vector<String> > data) throws Exception {
		String xmlData = "";
		String dirPath = "res/templates/" + tmpltName;
		File dir = new File(dirPath);
		if(!dir.exists())
			dir.mkdirs();
		String tmpltFileName = dirPath + "/tmplt_" + tmpltName + ".xml";
		Document docTmplt = createDocFromFactory();
		Element root = docTmplt.createElement("COMMAND_XML_LIST");
		root.setAttribute("count", String.valueOf(data.size()));
		docTmplt.appendChild(root);
		
		this.updateRequest(docTmplt, tmpltFileName);
		//docTmplt.getDocumentElement().removeChild(null);
		for(int i = 0; i < data.size(); i++)
		{
			Vector<String> row = (Vector<String>)data.elementAt(i);
			String cmdName = row.elementAt(0);
			String count = row.elementAt(1);
			String cmd = "COMMAND" + (i + 1);
			Element node = docTmplt.createElement(cmd);
			root.appendChild(node);
			Element nameNode = docTmplt.createElement("NAME");
			Element counterNode = docTmplt.createElement("COUNTER");
			nameNode.setTextContent(cmdName);
			counterNode.setTextContent(count);
			node.appendChild(nameNode);
			node.appendChild(counterNode);
		}
		this.updateRequest(docTmplt, tmpltFileName);
		if(! tmpltList.contains(tmpltName))
			this.tmpltList.addElement(tmpltName);
		return xmlData;
	}
	
	public void updateTemplate(String tmpltName, String xmlData) throws Exception {
		this.tmpltList.addElement(tmpltName);
		//create a new dir & File
		String dirPath = "res/templates/" + tmpltName;
		File dir = new File(dirPath);
		if(!dir.exists())
			dir.mkdirs();
		String tmpltFilePath = dirPath + "/tmplt_" + tmpltName + ".xml";
		File tmpltFile = new File(tmpltFilePath);
		if(!tmpltFile.exists())
			tmpltFile.createNewFile();
		this.saveXmlData(xmlData, tmpltFilePath);
		//create log file
		String logFilePath = dirPath + "/log_" + tmpltName + ".xml";
		File logFile = new File(logFilePath);
		if(!logFile.exists())
			logFile.createNewFile();
	}
	
	public void showTmpltList() {
		logger.info("size: " + this.tmpltList.size());
		for(int i = 0; i < this.tmpltList.size(); i++)
			logger.info(tmpltList.getElementAt(i));
	}
}
