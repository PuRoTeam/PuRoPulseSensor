package it.uniroma2.pulsesensor.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.*;

public class AES 
{	
	public static final String algorithm = "AES";
	public static final String transformation = "AES/CBC/PKCS7Padding";//ECB, NoPadding, PKCS5Padding, PKCS7Padding
	public static final String providerName = "BC"; //BC
	public static final Provider provider = new BouncyCastleProvider();

	public static String EncryptIVFromKey(String plainText, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException
	{
		String digest = SHA256.getMsgDigest(key);
		String iv = digest.substring(0, digest.length()/2);
		
		return Encrypt(plainText, key, iv);
	}
			
	public static String Encrypt(String plainText, String key, String iv)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException 
	{				
		byte keyInByte[] = Utility.hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi
		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, algorithm);
		
		byte ivBytes[] = Utility.hexStringToByteArray(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

		Cipher cipher = null;
		if(!providerName.equals(""))
			cipher = Cipher.getInstance(transformation, providerName);
		else
			cipher = Cipher.getInstance(transformation);		
				
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		 
		byte[] plainTextBytes = Utility.stringToBytes(plainText);
		
		byte[] encryptedTextBytes = cipher.doFinal(plainTextBytes); //per il plainText possiamo usare anche questa funzione
		
		return Hex.encodeHexString(encryptedTextBytes);
	}
	
	public static String DecryptIVFromKey(String plainText, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException
	{
		String digest = SHA256.getMsgDigest(key);
		String iv = digest.substring(0, digest.length()/2);
		
		return Decrypt(plainText, key, iv);
	}
	
	public static String Decrypt(String encryptedText, String key, String iv) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException   
	{	
		byte keyInByte[] = Utility.hexStringToByteArray(key); //necessario per riottenere un array di 32 elementi 
		SecretKeySpec keySpec = new SecretKeySpec(keyInByte, algorithm);
		
		byte ivBytes[] = Utility.hexStringToByteArray(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		
		Cipher cipher = null;
		if(!providerName.equals(""))
			cipher = Cipher.getInstance(transformation, providerName);
		else
			cipher = Cipher.getInstance(transformation);	
				
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		 
		byte[] encryptedTextBytes = Utility.hexStringToByteArray(encryptedText);
		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		
		return Utility.bytesToString(decryptedTextBytes);
	}
	
	/*--------------------------------------------------*/
	
	public static void main(String[] args) 
	{	
		if(Security.getProvider(providerName) == null);
			Security.addProvider(provider);
		
		encryptDecrypt();
	}
		
	public static void encryptDecrypt()
	{	
		String plainText = "";
		System.out.println("PlainText: " + plainText);
			
		//byte keyByte[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		//String key = Hex.encodeHexString(keyByte);		
		String key = "19581e27de7ced00ff1ce50b2047e7a567c76b1cbaebabe5ef03f7c3017bb5b7";
		System.out.println("Key: " + key);
		
		try
		{
		    String encryptedText = EncryptIVFromKey(plainText, key);
		    //encryptedText = "7beee3525ca4569c1ffd1bea18af5e40";
		    System.out.println("EncryptedText: " + encryptedText);
		    String decryptedText = DecryptIVFromKey(encryptedText, key);
		    System.out.println("DecryptedText: " + decryptedText);
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
}
