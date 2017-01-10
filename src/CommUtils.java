import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;


public class CommUtils{

	static Socket client;
	static Logger logger;
	static String respTimeout;
	static String address;
	static Thread threadRetry;
	static RetryConnect	retryconnect;
	static String readData;
	static boolean dataAvailable;

	public CommUtils(String clientAddr, String resptimeout, Logger log, boolean initReqd) throws Exception {
		if(initReqd)
		{
			logger = log;
			respTimeout = resptimeout;
			address = clientAddr;
			readData = "";
			dataAvailable = false;
			
			client = new Socket(clientAddr, 5015);
			logger.info("Connected to Server " + client.getLocalAddress().getHostAddress());
			logger.info("Response time out " + respTimeout);
			
			retryconnect = new RetryConnect();
			threadRetry = new Thread(retryconnect);
			threadRetry.start();
			
			if(!respTimeout.isEmpty())
				client.setSoTimeout(Integer.valueOf(respTimeout));
		}

	}
	
	public void sendMessage(String request){
		try {
			logger.info(client.isOutputShutdown() + " " + client.isBound() + " " + client.isClosed() + " " + " " + client.isConnected());

			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.write(request.getBytes("UTF-8"));
		}
		catch(Exception e) {
			logger.info("Failed to send SCI request. Disconnect from POS");
		}

	}
	
	public String recvMessage() {
		try{
			while(! dataAvailable)
				Thread.sleep(10);
			dataAvailable = false;
			return readData;
			
//			DataInputStream input = new DataInputStream(client.getInputStream());
//			byte[] data = new byte[8192];
//			int read = input.read(data);
//			//logger.info("Bytes read " + read);
//			String res = new String(data, "UTF8");
//			res = res.replaceAll("\\n", "");
//			return res.substring(0, res.lastIndexOf(">") + 1);
		}
//		catch(SocketTimeoutException e) {
//			logger.info("Response Timeout From device");
//			return "";	
//		}
		catch(Exception e) {
			logger.info("Disconnect from POS");
			return "";	
		}
	}
	public boolean reConnect() {
		try {
			logger.info("Re Connecting From Server");
			client.close();
			client = new Socket(address, 5015);
			if(!respTimeout.isEmpty())
				client.setSoTimeout(Integer.valueOf(respTimeout));
			logger.info("Connected to Server " + client.getLocalAddress().getHostAddress());
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
