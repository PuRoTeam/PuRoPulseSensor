package it.uniroma2.pulsesensor.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class SHA256 
{	
	public static String getMsgDigest(String msg)
	{
		byte byteDigest[] = getByteMsgDigest(msg);
		String msgDigest = Hex.encodeHexString(byteDigest);
        		
        return msgDigest;
	}
	
	public static byte[] getByteMsgDigest(String msg)
	{
        MessageDigest md;
        byte byteDigest[] = null;
		try 
		{
			md = MessageDigest.getInstance("SHA-256");
			md.update(Utility.stringToBytes(msg)); 
			byteDigest = md.digest();
		}
		catch (NoSuchAlgorithmException e) 
		{ e.printStackTrace(); }
        
        return byteDigest;
	}
	
    public static void main(String[] args) throws Exception
    {
		myExample();
    }
    
    public static void myExample() throws Exception
    {
    	byte keyByte[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    	String key = Hex.encodeHexString(keyByte);
    	System.out.println(key);
    	
    	String digest = getMsgDigest(key);
    	
    	System.out.println(digest);
    	System.out.println(digest.length());
    }
}
