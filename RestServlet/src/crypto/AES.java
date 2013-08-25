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

public class AES 
{
	private static String Encrypt(String plainText, String key) throws 
    NoSuchAlgorithmException, 
    NoSuchPaddingException, 
    InvalidKeyException, 
    IllegalBlockSizeException, 
    BadPaddingException, 
    UnsupportedEncodingException 
{
    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
     
    // Instantiate the cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
     
    byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
     
    return new Base64().encodeAsString(encryptedTextBytes);
}
 
private static String Decrypt(String encryptedText, String key) throws 
    NoSuchAlgorithmException, 
    NoSuchPaddingException, 
    InvalidKeyException, 
    IllegalBlockSizeException, 
    BadPaddingException, 
    UnsupportedEncodingException  
{
     
    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
 
    // Instantiate the cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
     
    byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText);
    byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
     
    return new String(decryptedTextBytes);
}
 
public static void main(String[] args) {
     
    String plainText = "Hello World";
    String key = "770A8A65DA156D24EE2A093277530142";
     
    System.out.println("Plain Text: " +plainText);
     
    // Encryption
    String encryptedText = null;
    try {
        encryptedText = Encrypt(plainText, key);
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
        String decryptedText = Decrypt(encryptedText, key);
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
}
}
