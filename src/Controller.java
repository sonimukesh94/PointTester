import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;

import javafx.scene.NodeBuilder;

import org.w3c.dom.*;

import sun.misc.Queue;
//import Message.sourceType;









import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.sun.xml.internal.bind.v2.Messages;

public class Controller{

	static XmlUtils xmlutils;
	static CommUtils commutils;
	static EncryptionUtils encryptutils;
	static GuiMgr guimgrutils;
	static AddCmdWindow addcmdwindow;
	static AddTemplateWindow addtmpltwindow;
	static Commands commands;
	static Templates templates;
	static TestWindow2 testwin2;
	static Queue<Message> queue;
	static ExecTemplate exectemplate;
	static Logger logger;	
	
	static Thread threadExecTmplt;
	static Vector<Vector<String> > dataExecTemplt;
	
	
	public static Vector<Vector<String>> getDataExecTemplt() {
		return dataExecTemplt;
	}

	public static void setDataExecTemplt(Vector<Vector<String>> dataExecTemplt) {
		Controller.dataExecTemplt = dataExecTemplt;
	}

	static int counter ;
	static String template;
	static String resptimeout;
	
	
	public Controller() throws Exception {
		counter = 1;
		template = "";
	}
	
	public int init() throws Exception{
	
		        
		Logs.logging();
		logger = Logger.getLogger("TesterLogs");
//	    SimpleFormatter formatter = new SimpleFormatter();  
//	    fhLogger.setFormatter(formatter); 
	        
	    logger.info("########### Controller Init Started ###############");
		xmlutils = new XmlUtils(logger);
		// generate RSA public & private key pairs
		encryptutils = new EncryptionUtils(logger);
		
		queue = new Queue<Message>();
		guimgrutils = new GuiMgr(queue,logger);
		//commutils = new CommUtils(guimgrutils.getTextField().getText());
		commands = new Commands();
		templates = new Templates();
		addcmdwindow = new AddCmdWindow(queue, xmlutils,logger);
		
		addtmpltwindow = new AddTemplateWindow(queue, xmlutils, commands,logger);
		
		exectemplate = new ExecTemplate();
				
		guimgrutils.initTemplateList(templates.tmpltList);
		
//		commands.showCommandList();
//		templates.showTmpltList();
		//addtmpltwindow.showAddTmpltWin(commands);
		
//		testwin = new TestWindow();
//		
//		testwin2 = new TestWindow2();
		
		logger.info("########### Controller Init Done ###############");
		
		return 0;
	}
	
	public boolean pair() throws Exception{
	
		commutils = new CommUtils(guimgrutils.getTextField().getText(), resptimeout, logger, true);
		// parse the REGISTER request
		Document docRegReuest = xmlutils.getRequest("res/init/registerReqs.xml");
		xmlutils.convertToString(docRegReuest);
		logger.info("#########################################");
		
		// add KEY to REGISTER request  
		Node key = docRegReuest.getElementsByTagName("KEY").item(0);
		key.setTextContent(encryptutils.getPublicKeyString());
		
		xmlutils.convertToString(docRegReuest);
		logger.info("#########################################");
		// update the request file back
		xmlutils.updateRequest(docRegReuest, "res/init/registerReqs.xml");
		
		// Send Request Message
		commutils.sendMessage(xmlutils.convertToString(docRegReuest));
		//commutils.sendMessage("<TRANSACTION><FUNCTION_TYPE>SECURITY</FUNCTION_TYPE><COMMAND>REGISTER</COMMAND><ENTRY_CODE>8962</ENTRY_CODE><REG_VER>1.0</REG_VER><KEY>MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOb3TxQKFJBAnmLIuCmop3/69eky3XV26MXJxSre0Xt9e7Hb1TorAVEOOubiIbJjk0Gcr1uknPZMmmX4wpu43HYfKhI+awVrr9As9RqbTnihtF0xpfWtbaqeOKAsRBvnRfChYWNbpnB4e3zIfdiFyl88MqcP7ecMdS0cDdhGZLQwIDAQAB</KEY></TRANSACTION>");
		// receive message
		logger.info("Waiting for response");
		String RegResponse = commutils.recvMessage();
		//RegResponse = RegResponse.replaceAll("\\n", "");
		logger.info("SCI RESPONSE: " + RegResponse);
		if(RegResponse.isEmpty())
			return false;
		
		Document docRegResponse = xmlutils.saveXmlData(RegResponse, "res/init/registerResp.xml");
		
		if(!docRegResponse.getDocumentElement().getElementsByTagName("RESULT_CODE").item(0).getTextContent().equals("-1"))	//Failure Case
			return false;
		//  parse for MAC_LBL
		Document docSecurity = xmlutils.getRequest("res/init/security.xml");
		Node macLbl = docSecurity.getElementsByTagName("MAC_LABEL").item(0);
		macLbl.setTextContent(docRegResponse.getElementsByTagName("MAC_LABEL").item(0).getTextContent());
		
		// parse for AES key
		String macKeyStr = docRegResponse.getElementsByTagName("MAC_KEY").item(0).getTextContent();
		String aesKeyStr = encryptutils.decrypt(macKeyStr, encryptutils.getPrivateKey());
		Node aesKey = docSecurity.getElementsByTagName("AES_KEY").item(0);
		aesKey.setTextContent(aesKeyStr);
		
		// update security file
		xmlutils.updateRequest(docSecurity, "res/init/security.xml");
		
		logger.info(DatatypeConverter.printHexBinary(Base64.decode(aesKeyStr.getBytes("UTF-8"))));
		guimgrutils.getBtnStart().setEnabled(true);
		return true;
	}
	
	public int unPair() throws Exception{
		
		return 0;
	}
	
	public void start() throws Exception{
///*	
		boolean execTemplate = false;
		while(true)
		{
			if(queue.isEmpty())
			{
				Thread.sleep(500);
				continue;
			}
			else
			{
				Message request = queue.dequeue();
				execTemplate = false;
				if(request.iMsgType == Message.msgType.BUTTON)
				{
					if(request.iSource == Message.sourceType.PAIR)
					{
						guimgrutils.getBtnPair().setEnabled(false);
						if(this.pair() == false)
							guimgrutils.getBtnPair().setEnabled(true);
						else
							guimgrutils.getBtnUnPair().setEnabled(true);
					}
					else if(request.iSource == Message.sourceType.STARTSESS)
					{
						template = "startSession";
						execTemplate = true;
					}
					else if(request.iSource == Message.sourceType.FINISHSESS)
					{
						template = "finishSession";
						execTemplate = true;
					}
					else if(request.iSource == Message.sourceType.RUN)
					{
						logger.info("Running Template");
						template = (String)guimgrutils.getSelectTemplate().getSelectedItem();
						logger.info(template);
						execTemplate = true;
						guimgrutils.getBtnStart().setEnabled(false);
						guimgrutils.getBtnStop().setEnabled(true);
						guimgrutils.getTextFieldTmpltRetries().setEnabled(false);
					}
					else if(request.iSource == Message.sourceType.STOP)
					{						
						if(threadExecTmplt.isAlive())
						{
							logger.info("Stoping Exec template");
							guimgrutils.getBtnStop().setEnabled(false);
							guimgrutils.getBtnStart().setEnabled(true);
							guimgrutils.getTextFieldTmpltRetries().setEnabled(true);
							threadExecTmplt.interrupt();
						}
						else
						{
							logger.info("Ignoring...Nothing is Running");
						}
						
						
					}
					else if(request.iSource == Message.sourceType.ADDCMD)
					{
						addcmdwindow.showAddCmdWin();
					}	
					else if(request.iSource == Message.sourceType.ADDTEMPLATE)
					{
						addtmpltwindow.showAddTmpltWin(commands);
					}
					else if(request.iSource == Message.sourceType.EDITTEMPLATE)
					{
						String templtName = (String)guimgrutils.getSelectTemplate().getSelectedItem();
						addtmpltwindow.showEditTmpltWin(templtName, templates.parseTemplate(templtName));
					}
				}
				else if(request.iMsgType == Message.msgType.SERVICE)
				{
					if(request.iSource == Message.sourceType.ADDCMD)
					{
						//xmlutils.validateXml(request.data);
						if(request.iServiceType == Message.serviceType.SAVE)
						{
							String cmdName = addcmdwindow.getCmdName();
							String xmlData = addcmdwindow.getXmlInput();
							logger.info("Adding new Cmd: " + cmdName);
							if(addcmdwindow.isLICmd())
							{
								commands.saveCommand(cmdName, xmlData, addcmdwindow.getStartLIId(),
										addcmdwindow.getEndLIId());
							}
							else
							{
								commands.saveCommand(cmdName, xmlData);	
							}							
							logger.info("Saved command");
							addtmpltwindow.updateTmpltListModel(cmdName);
							addcmdwindow.dispose();
						}
					}
					else if(request.iSource == Message.sourceType.ADDTEMPLATE)
					{
						if(request.iServiceType == Message.serviceType.INSERT)
						{
							addtmpltwindow.insertCmdToDestList();
						}
						else if(request.iServiceType == Message.serviceType.DELETE)
						{
							addtmpltwindow.deleteCmdFromDestList();
						}
						else if(request.iServiceType == Message.serviceType.DELETEALL)
						{
							addtmpltwindow.deleteAllCmdFromDestList();
						}
						else if(request.iServiceType == Message.serviceType.SAVE)
						{
							logger.info("Saving Template " + addtmpltwindow.getTemplateName());
							String xmlData = templates.saveTemplate(addtmpltwindow.getTemplateName(), addtmpltwindow.tableModel.getDataVector());
							//templates.saveTemplate(addtmpltwindow.getTemplateName(), xmlData);
							logger.info("Saved Template");
							guimgrutils.initTemplateList(templates.tmpltList);
							guimgrutils.getSelectTemplate().setSelectedItem(addtmpltwindow.getTemplateName());
							addtmpltwindow.dispose();
						}
					}
				}

			}
			if(execTemplate == true)
			{
				logger.info("Starting Exec template");
				// receive the request => parse it => perform it => send the response back
				dataExecTemplt = xmlutils.parseTemplate(template);
				logger.info("Size template " + dataExecTemplt.size());
				//Create separate thread to execute the current template
				threadExecTmplt = new Thread(exectemplate);
				threadExecTmplt.start();
				

			}
		}
//*/
		/*
		while(true)
		{
			logger.info("Start template");
			// receive the request => parse it => perform it => send the response back
			template = "safRemoval";
			DefaultListModel<String> cmdList = xmlutils.parseTemplate(template);
			logger.info("Size template " + cmdList.size());
			for(int index = 0; index < cmdList.size(); index++)
			{
				logger.info("commands \"" + cmdList.getElementAt(index) + "\"");
				String cmdFileName = "res/commands/" + cmdList.getElementAt(index).replaceAll("\\n\r", "") + ".xml";
				logger.info("commands " + cmdFileName);
				Document docCmdRequest = xmlutils.appendSecurityToRequest(cmdFileName, getCounter(), encryptutils.encodeHMAC(xmlutils.getAESKey(), getCounter()));
				incrCounter();
				// Send Request Message
				commutils.sendMessage(xmlutils.convertToString(docCmdRequest));
				logger.info("Waiting for response");
				String cmdResp = commutils.recvMessage();
				logger.info(cmdResp);
				//Document docRegResponse = xmlutils.saveXmlData(cmdResp, "res/templates/dispMessage/res_dispMsg.xml");
			}
			//break;
		}
		*/
	}
	
	public static String getCounter(){
		return Integer.toString(counter);
	}

	public static void incrCounter(){
		counter++;
	}
	
	static public PrintWriter initLogging(PrintWriter out) throws Exception {
        File fh = new File("res/stackTraces.txt");
        fh.delete();
        fh.createNewFile();
        FileWriter fw = new FileWriter("res/stackTraces.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        out = new PrintWriter(bw);
        return out;
	}
	
	public static void main(String[] args) {
			PrintWriter out = null;
			resptimeout = "";
			if(args.length > 0)
			{
				for (String arg : args)
				{
					if(arg.startsWith("responseTimeout="))
						resptimeout = arg.substring(arg.indexOf('=') + 1, arg.length());
					break;
				}				
			}
				
		try{
			out = initLogging(out);

			Controller controller = new Controller();
			
			controller.init();
			logger.info("response timeout=" + resptimeout);
			//controller.pair();
			
			controller.start();
		}
		catch(Exception e)
		{	        
			e.printStackTrace(out);
			out.close();
			//logger.info(e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "System Error ! Please Restart...");
			guimgrutils.dispose();
		}


	}

}

