package crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class ArduinoAES 
{
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
}
