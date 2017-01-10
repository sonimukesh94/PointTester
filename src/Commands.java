import java.io.File;

import javax.swing.DefaultListModel;








import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import sun.security.pkcs11.wrapper.Functions;




public class Commands extends XmlUtils{
	private DefaultListModel<String> cmdList = new DefaultListModel<String>();
	private	int iLINumber;
	private	int iOfferLINumber;

	public int getiOfferLINumber() {
		return iOfferLINumber;
	}

	public void setiOfferLINumber(int iOfferLINumber) {
		this.iOfferLINumber = iOfferLINumber;
	}

	public int getiLINumber() {
		return iLINumber;
	}

	public void setiLINumber(int iLINumber) {
		this.iLINumber = iLINumber;
	}

	public DefaultListModel<String> getCmdList() {
		return cmdList;
	}

	public void setCmdList(DefaultListModel<String> cmdList) {
		this.cmdList = cmdList;
	}

	public Commands() {
		super(logger);
		//load all the commands name from "res/commands" directory
		File dir = new File("res/commands");
		File[] list = dir.listFiles();
		
		logger.info("size of list of commands " );
		logger.info("size of list of commands " + list.length);
		for(int i = 0; i < list.length && list[i].isFile(); i++)
			this.cmdList.addElement(list[i].getName().substring(0, list[i].getName().length() - 4));
	}

	
	public void saveCommand(String cmdName, String xmlData) throws Exception {
		this.cmdList.addElement(cmdName);
		//create a new File
		String destFilePath = "res/commands/" + cmdName + ".xml";
		logger.info(destFilePath);
		File file = new File(destFilePath);
		if(!file.exists())
			file.createNewFile();
		this.saveXmlData(xmlData, destFilePath);
	}

	public void saveCommand(String cmdName, String xmlData, String startId, String endId) throws Exception {
		this.cmdList.addElement(cmdName);
		//create a new File
		String destFilePath = "res/commands/" + cmdName + ".xml";
		logger.info(destFilePath);
		File file = new File(destFilePath);
		if(!file.exists())
			file.createNewFile();
		this.saveXmlData(xmlData, destFilePath);
		
		Document doc = getRequest(destFilePath);
		doc.getDocumentElement().setAttribute("endId", endId);
		doc.getDocumentElement().setAttribute("startId", startId);
		
		this.updateRequest(doc, destFilePath);
	}
	
	public boolean isLICmd(String cmdFilePath) throws Exception {
		Document doc = getRequest(cmdFilePath);
		String startId = doc.getDocumentElement().getAttribute("startId");
		String endId = doc.getDocumentElement().getAttribute("endId");
		if(! startId.isEmpty() || ! endId.isEmpty())
			return true;
		else
			return false;
	}
	
	public Document prepareLICmd(Document doc)
	{
		String startId = doc.getDocumentElement().getAttribute("startId");
		String endId = doc.getDocumentElement().getAttribute("endId");
		
		logger.info("Preparing LI Command for startId " + startId + " EndLIId " + endId);
		
		int iStartId = Integer.valueOf(startId) + this.getiLINumber(); // add offset. LI numbers must not be repeated
		int iEndId = Integer.valueOf(endId) + this.getiLINumber(); // add offset. LI numbers must not be repeated
		this.setiLINumber(this.getiLINumber() + (iEndId - iStartId) + 1);	// increment the LI number
		
		if(iStartId == iEndId || iStartId == 0 || iEndId == 0)
		{
			int iLiId = iStartId != 0 ? iStartId : iEndId;
			logger.info("Single LI ID " + iLiId);
			doc.getDocumentElement().getElementsByTagName("LINE_ITEM_ID").item(0).setTextContent(String.valueOf(iLiId));
			doc.getDocumentElement().getElementsByTagName("DESCRIPTION").item(0).setTextContent("LINE ITEM " + String.valueOf(iLiId));
		}
		else
		{
			logger.info("Creating Nested LI Request ");
			boolean isOfferNode = false;
			int		iStartOfferLINo = 0;
			int		iEndOfferLINo = 0;
			NodeList LINodeList = doc.getDocumentElement().getElementsByTagName("MERCHANDISE");
			if(LINodeList.getLength() == 0)
			{
				isOfferNode = true;
				LINodeList = doc.getDocumentElement().getElementsByTagName("OFFER");
				if(LINodeList.getLength() == 0)
				{
					return doc;
				}
			}
			Element node = (Element)LINodeList.item(0);
			
			node.getElementsByTagName("LINE_ITEM_ID").item(0).setTextContent(String.valueOf(iStartId));
			//node.getElementsByTagName("DESCRIPTION").item(0).setTextContent("LINE ITEM " + String.valueOf(iStartId));
			if(isOfferNode)
			{
				if(this.getiOfferLINumber() == 0) // For first time take seed value of Offer Line item from Input command request
					iStartOfferLINo = Integer.valueOf(doc.getDocumentElement().getElementsByTagName("OFFER_LINE_ITEM").item(0).getTextContent());
				else
					iStartOfferLINo = this.getiOfferLINumber();
				
				iEndOfferLINo = iStartOfferLINo + (iEndId - iStartId);
				this.setiOfferLINumber(iEndOfferLINo + 1);	// increment the Offer LI number
				node.getElementsByTagName("OFFER_LINE_ITEM").item(0).setTextContent(String.valueOf(iStartOfferLINo));
				node.getElementsByTagName("DESCRIPTION").item(0).setTextContent("OFFER " + String.valueOf(iStartId));
			}
			else
				node.getElementsByTagName("DESCRIPTION").item(0).setTextContent("LINE ITEM " + String.valueOf(iStartId));
			for(int i = iStartId + 1, j = iStartOfferLINo + 1; i <= iEndId ; i++, j++)
			{
				Element newNode = (Element)node.cloneNode(true);
				newNode.getElementsByTagName("LINE_ITEM_ID").item(0).setTextContent(String.valueOf(i));
				//newNode.getElementsByTagName("DESCRIPTION").item(0).setTextContent("LINE ITEM " + String.valueOf(i));
				if(isOfferNode)
				{
					newNode.getElementsByTagName("OFFER_LINE_ITEM").item(0).setTextContent(String.valueOf(j));
					newNode.getElementsByTagName("DESCRIPTION").item(0).setTextContent("OFFER " + String.valueOf(i));
				}
				else
					newNode.getElementsByTagName("DESCRIPTION").item(0).setTextContent("LINE ITEM " + String.valueOf(i));
				doc.getDocumentElement().getElementsByTagName("LINE_ITEMS").item(0).appendChild(newNode);
			}
		}
		return doc;
	}
	public void showCommandList() {
		logger.info("size: " + this.cmdList.size());
		for(int i = 0; i < this.cmdList.size(); i++)
			logger.info(cmdList.getElementAt(i));
	}

}
