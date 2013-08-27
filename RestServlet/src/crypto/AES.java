package crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AES 
{
	/*public static String EncryptOriginal(String plainText, String key) throws 
				NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
				IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException 
    {
	    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	     
	    // Instantiate the cipher
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
	     
	    byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
	     
	    return new Base64().encodeAsString(encryptedTextBytes);
    }
	
	public static String DecryptOriginal(String encryptedText, String key) throws 
				NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
				IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException 
	{
	    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	 
	    // Instantiate the cipher
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, keySpec);
	     
	    byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText);
	    byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
	     
	    return new String(decryptedTextBytes);
	}*/
 
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
	
	public static String Encrypt(String plainText, String key) throws 
				NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
				IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException  
	{
		byte keyInByte[] = hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi
		
		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, "AES");
		 
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		 
		byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8")); //per il plainText possiamo usare anche questa funzione
		 
		return new Base64().encodeAsString(encryptedTextBytes);
	}	

	public static String Decrypt(String encryptedText, String key) throws 
				NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
				IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException   
	{
		byte keyInByte[] = hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi 
		
		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, "AES");
		
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		 
		byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText);
		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		 
		return new String(decryptedTextBytes);
	}

	public static void main(String[] args) 
	{
		myExample();
	}

	public static void myExample()
	{
		String plainText = "123456";
	    MessageDigest md;
		try 
		{			
			String key = SHA256.getMsgDigest(plainText);			
		    System.out.println(key);
		    
		    String encryptedText = Encrypt(plainText, key);
		    System.out.println(encryptedText);
		    
		    String decryptedText = Decrypt(encryptedText, key);
		    System.out.println(decryptedText);
		} 
		catch (Exception e) 
		{ e.printStackTrace(); }
	}

	/*public static void internetExample()
	{
		   String plainText = "Hello World";
		    String key = "770A8A65DA156D24EE2A093277530142";
		    //String key =   "8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92";
		    
		    System.out.println("Lunghezza chiave " + key.length());
		    System.out.println("Plain Text: " +plainText);
		     
		    // Encryption
		    String encryptedText = null;
		    try {
		        encryptedText = EncryptOriginal(plainText, key);
		        System.out.println("Encrypted Text: " +encryptedText);
		    } catch (InvalidKeyException e) {
		        e.printStackTrace();
		    } catch (NoSuchAlgorithmException e) {
		        e.printStackTrace();
		    } catch (NoSuchPaddingException e) {
		        e.printStackTrace();
		    } catch (IllegalBlockSizeException e) {
		        e.printStackTrace();
		    } catch (BadPaddingException e) {
		        e.printStackTrace();
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		    }
		     
		    // Decryption
		    try {
		        String decryptedText = DecryptOriginal(encryptedText, key);
		        System.out.println("Decrypted Text: " +decryptedText);
		    } catch (InvalidKeyException e) {
		        e.printStackTrace();
		    } catch (NoSuchAlgorithmException e) {
		        e.printStackTrace();
		    } catch (NoSuchPaddingException e) {
		        e.printStackTrace();
		    } catch (IllegalBlockSizeException e) {
		        e.printStackTrace();
		    } catch (BadPaddingException e) {
		        e.printStackTrace();
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		    }
	}*/
}
