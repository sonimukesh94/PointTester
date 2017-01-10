import java.io.DataInputStream;

import java.net.SocketTimeoutException;




public class RetryConnect extends CommUtils implements Runnable{
	
	
	public RetryConnect() throws Exception{
		super(address, respTimeout, logger, false);
		logger.info("Creating thread for Retry Connect");
	}
	
	public void run() { 
		try {
			
			DataInputStream input = new DataInputStream(client.getInputStream());
			
			while(true) {
				
				byte[] data = new byte[8192];
				int read = input.read(data);
				if(read == -1)
				{
					while(! this.reConnect())
						Thread.sleep(2000);
					input = new DataInputStream(client.getInputStream());
					continue;
				}
					
				//logger.info("Bytes read " + read);
				String res = new String(data, "UTF8");
				res = res.replaceAll("\\n", "");
				readData = res.substring(0, res.lastIndexOf(">") + 1);
				dataAvailable = true;
			}
				
		}
		catch(SocketTimeoutException e) {
			logger.info("Response Timeout From device");
			readData = "";
			dataAvailable = true;
		}
		catch(Exception e) {
			e.printStackTrace();
			readData = "";
			dataAvailable = true;
		}
	}

}
