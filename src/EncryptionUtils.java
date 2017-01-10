//import com.sun.java.util.jar.pack.Package.File;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;



public class EncryptionUtils {

	String ALGORITHM = "RSA";
	String RSA_PUBLIC_KEY = "keys/public.key";
	String RSA_PRIVATE_KEY = "keys/private.key";
//	String RSA_PUBLIC_KEY = "C:/keys/public.key";
//	String RSA_PRIVATE_KEY = "C:/keys/private.key";
	static Logger logger;
	
	public EncryptionUtils(Logger log) throws Exception{
		logger = log;
		if(this.isKeysPresent())
			this.deleteKeys();
		this.generateKey();
	}
	
	public void deleteKeys() throws Exception{
		// create public & private key file
		File publicKeyFile = new File(RSA_PUBLIC_KEY);
		File privateKeyFile = new File(RSA_PRIVATE_KEY);
		publicKeyFile.delete();
		privateKeyFile.delete();
	}
	
	public void generateKey() throws Exception{
		// generate key pair
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		//keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();

		// create public & private key file
		File publicKeyFile = new File(RSA_PUBLIC_KEY);
		File privateKeyFile = new File(RSA_PRIVATE_KEY);
		
		if(publicKeyFile.getParentFile() != null)
			publicKeyFile.getParentFile().mkdirs();
		publicKeyFile.createNewFile();
		
		if(privateKeyFile.getParentFile() != null)
			privateKeyFile.getParentFile().mkdirs();
		privateKeyFile.createNewFile();
		
		//save the keys in the file
		ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
		publicKeyOS.writeObject(key.getPublic());
		publicKeyOS.close();
		
		ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
		privateKeyOS.writeObject(key.getPrivate());
		privateKeyOS.close();
	}
	
	public boolean isKeysPresent() {
		File publicKeyFile = new File(RSA_PUBLIC_KEY);
		File privateKeyFile = new File(RSA_PRIVATE_KEY);
		if(publicKeyFile.exists() && privateKeyFile.exists())
			return true;
		else
			return false;
	}
	
	public String encrypt(String data, PublicKey key) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] dataNew = cipher.doFinal(data.getBytes("UTF8"));
		return Base64.encode(dataNew);
	}
	
	public String decrypt(String data, PrivateKey key) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] dataNew = cipher.doFinal(Base64.decode(data));
		return Base64.encode(dataNew);
		//return new String(dataNew, "UTF8");	
	}
	
	public String encodeHMAC(byte[] key, String data) throws Exception {
		  Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		  SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
		  sha256_HMAC.init(secret_key);
		  return Base64.encode(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
		  
		}
	
	public PublicKey getPublicKey() throws Exception{
		ObjectInputStream inputStream = null;
		inputStream = new ObjectInputStream(new FileInputStream(this.RSA_PUBLIC_KEY));
		PublicKey publicKey = (PublicKey) inputStream.readObject();
		inputStream.close();
		return publicKey;
	}
	
	public String getPublicKeyString() throws Exception{
		ObjectInputStream inputStream = null;
		inputStream = new ObjectInputStream(new FileInputStream(this.RSA_PUBLIC_KEY));
		PublicKey publicKey = (PublicKey) inputStream.readObject();
		inputStream.close();
		return Base64.encode(publicKey.getEncoded());
	}
	
	public PrivateKey getPrivateKey() throws Exception{
		ObjectInputStream inputStream = null;
		inputStream = new ObjectInputStream(new FileInputStream(this.RSA_PRIVATE_KEY));
		PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		inputStream.close();
		return privateKey;
	}
	
}
