import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Logs {
	static FileHandler fileTxt;
	static SimpleFormatter formatterTxt;
	static Logger logger;


	static public void logging() throws IOException {
	
		String FILE_NAME = "res/debug.txt";
//		String FILE_NAME = "C:/Users/T_MukeshS3/workspace/Java/debug.txt";
		File fh = new File(FILE_NAME);
		fh.delete();
		fh.setExecutable(true);
		fh.createNewFile();
	    logger = Logger.getLogger("");
	    logger.setLevel(Level.INFO);
	    fileTxt = new FileHandler(FILE_NAME);
	    //fileTxt = new FileHandler("/debug.log");
	    formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	    logger.addHandler(fileTxt);
	
	}
}