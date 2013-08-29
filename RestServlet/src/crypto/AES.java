package crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class AES 
{	
	public static final String algorithm = "AES";
	public static final String transformation = "AES";//"AES/CBC/NoPadding (128)";
	
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
	
	/*public static String EncryptBase64(String plainText, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	{
		//SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		byte keyInByte[] = hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi
		
		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, "AES");
		 
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		 
		byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8")); //per il plainText possiamo usare anche questa funzione
		
		return new Base64().encodeAsString(encryptedTextBytes);
	}

	public static String DecryptBase64(String encryptedText, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	{
		//SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		byte keyInByte[] = hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi 
		
		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, "AES");
		
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		 
		byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText);
		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		 
		return new String(decryptedTextBytes);
	}*/
	
	public static String Encrypt(String plainText, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	{
		byte keyInByte[] = hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi

		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, algorithm);
		 
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		 
		byte[] plainTextBytes = stringToBytes(plainText);
		
		byte[] encryptedTextBytes = cipher.doFinal(plainTextBytes); //per il plainText possiamo usare anche questa funzione
		
		return Hex.encodeHexString(encryptedTextBytes);
	}
	
	public static String Decrypt(String encryptedText, String key) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException  
	{
		byte keyInByte[] = hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi 
		
		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, algorithm);
		
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		 
		byte[] encryptedTextBytes = hexStringToByteArray(encryptedText);
		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		
		return bytesToString(decryptedTextBytes);
	}
	
	public static String bytesToString(byte a[])
	{
		String s = "";
		for(int i = 0; i < a.length; i++)		
			s += (char)a[i];		
		return s;
	}

	public static byte[] stringToBytes(String s)
	{
		byte[] a = new byte[s.length()];
		for(int i = 0; i < s.length(); i++)		
			a[i] = (byte)s.charAt(i);
		return a;
	}
	
	/*--------------------------------------------------*/
	
	public static void main(String[] args) 
	{
		//shaAndAesProva();
		encryptDecrypt();
		//prova();
	}
	
	public static void prova()
	{
		try
		{
			String message="Message to Decode";
	
			KeyGenerator key = KeyGenerator.getInstance("AES");
			key.init(256);
	
			SecretKey s = key.generateKey();
			byte[] raw = s.getEncoded();
			System.out.println(raw.length);
	
			SecretKeySpec sskey= new SecretKeySpec(raw, "AES");
	
			Cipher c = Cipher.getInstance("AES");
	
			c.init(Cipher.ENCRYPT_MODE, sskey);
	
			byte[] encrypted = c.doFinal(message.getBytes());
			System.out.println("encrypted string: " + Hex.encodeHexString(encrypted));
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
	
	public static void encryptDecrypt()
	{	
		String plainText = "2222222222222222";
		System.out.println("PlainText: " + plainText);
		
		byte keyByte[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32};
		String key = Hex.encodeHexString(keyByte);
		//String key = "770A8A65DA156D24EE2A093277530142";
		System.out.println("Key: " + key);
		try
		{
			//"dfbf252518738b0aafddedf1e010cd90";
		    String encryptedText = Encrypt(plainText, key);
		    System.out.println("EncryptedText: " + encryptedText);
		    
		    String decryptedText = Decrypt(encryptedText, key);
		    System.out.println("DecryptedText: " + decryptedText);
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
	
	public static void shaAndAesProva()
	{
		String plainText = "123456";
		System.out.println("PlainText: " + plainText);
		try 
		{			
			String key = SHA256.getMsgDigest(plainText);			
		    System.out.println("Key: " + key);
		    
		    String encryptedText = Encrypt(plainText, key);
		    System.out.println("EncryptedText: " + encryptedText);
		    
		    String decryptedText = Decrypt(encryptedText, key);
		    System.out.println("DecryptedText: " + decryptedText);
		} 
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
}
