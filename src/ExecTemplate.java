import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.w3c.dom.*;


public class ExecTemplate extends Controller implements Runnable{

	public ExecTemplate() throws Exception{
		logger.info("Creating thread for exec template");
	}
	public void run(){
		PrintWriter out = null;
		try{
			logger.info("Running Exec Template thread");
			
			// Initialize Log file
			String targetPath = "res/templates/" + template +  "/log_" + template + ".txt";
			logger.info(targetPath);
			File file = new File(targetPath);
			if(file.exists())
				file.delete();
			file.createNewFile();
			
	        FileWriter fw = new FileWriter(targetPath, true);
	        BufferedWriter bw = new BufferedWriter(fw);
	        out = new PrintWriter(bw);
	        
			//Initialize log table
	        guimgrutils.modelLogTable.setRowCount(0);
	        guimgrutils.addDataInLogTable("Timestamp", "Type", "Cmd/RespText", "Function/Rst");
			
			//Get the template data
			Vector<Vector<String> > data = getDataExecTemplt();
			logger.info("template size " + String.valueOf(data.size()));
			int maxTmpltRetry = 1;
			if(!guimgrutils.getTextFieldTmpltRetries().getText().isEmpty())
				maxTmpltRetry = Integer.valueOf(guimgrutils.getTextFieldTmpltRetries().getText());
			for(int tmpltRetries = 1; tmpltRetries <= maxTmpltRetry && ! Thread.currentThread().isInterrupted(); tmpltRetries++)
			{
				for(int index = 0; index < data.size() && ! Thread.currentThread().isInterrupted(); index++)
				{
					Vector<String> row = (Vector<String>)data.elementAt(index);
					String cmdName = row.elementAt(0);
					String count = row.elementAt(1);
					logger.info("command " + cmdName + " counter " + count);
					String cmdFileName = "res/commands/" + cmdName + ".xml";
					logger.info("commands " + cmdFileName);
					
					// Check for LI Command
					boolean bLICmd = commands.isLICmd(cmdFileName);
					commands.setiLINumber(0);
					
					for(int jIndex = 0; jIndex < Integer.valueOf(count) && ! Thread.currentThread().isInterrupted(); jIndex++)
					{
						
						Document docCmdRequest = xmlutils.appendSecurityToRequest(cmdFileName, getCounter(), encryptutils.encodeHMAC(xmlutils.getAESKey(), getCounter()));
						incrCounter();
						
						// Check for LI Command, Prepare LI nested request
						if(bLICmd)
						{
							docCmdRequest = commands.prepareLICmd(docCmdRequest);
						}
						
						//Save request to log file
						String strCmdRequest = xmlutils.convertToString(docCmdRequest);
						String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(Calendar.getInstance().getTime());
						out.println(timeStamp);
						xmlutils.saveXmlLogData(strCmdRequest, out);
						
						//Update Request to log table
						String function = docCmdRequest.getDocumentElement().getElementsByTagName("FUNCTION_TYPE").item(0).getTextContent();
						String command  = docCmdRequest.getDocumentElement().getElementsByTagName("COMMAND").item(0).getTextContent();
						guimgrutils.addDataInLogTable(timeStamp, "Request", command, function);
						

						// Send & Receive Message
						commutils.sendMessage(strCmdRequest);
						logger.info("Waiting for response");
						String strCmdResp = commutils.recvMessage();
						logger.info("SCI RESPONSE: " + strCmdResp);
						// Save response to log file
						String timeStamp2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(Calendar.getInstance().getTime());
						out.println(timeStamp2);
						
						if(strCmdResp.isEmpty())
						{
							guimgrutils.addDataInLogTable(timeStamp2, "Response", "SCI RESPONSE TIMEOUT", "DEVICE FROZEN");
							Thread.currentThread().interrupt();// stop this thread
							continue;
						}
							
						xmlutils.saveXmlLogData(strCmdResp, out);
						
						//Update response to log table
						Document docCmdResp = xmlutils.convertToXML(strCmdResp);
						String resultCode = docCmdResp.getDocumentElement().getElementsByTagName("RESULT_CODE").item(0).getTextContent();
						String respText   = docCmdResp.getDocumentElement().getElementsByTagName("RESPONSE_TEXT").item(0).getTextContent();
						guimgrutils.addDataInLogTable(timeStamp2, "Response", respText, resultCode);
						//Thread.sleep(500);
					}
				}	
				commands.setiLINumber(0);
				commands.setiOfferLINumber(0);
			}
			out.close();
			logger.info("Returning from Exec Template Thread");
			guimgrutils.getBtnStart().setEnabled(true);
			guimgrutils.getBtnStop().setEnabled(false);
			guimgrutils.getTextFieldTmpltRetries().setEnabled(true);
		}
		catch(InterruptedException ex) {
			ex.printStackTrace();
			logger.info("Interrupted ! Returning from Exec Template Thread");
			commands.setiLINumber(0);
			commands.setiOfferLINumber(0);
			out.close();
			return ;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "System Error ! Please Restart...");
			guimgrutils.dispose();
			out.close();
			return ;
		}
		
	}
	
}



