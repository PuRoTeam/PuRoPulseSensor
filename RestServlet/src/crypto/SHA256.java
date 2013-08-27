package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
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
			md.update(msg.getBytes()); 
			byteDigest = md.digest();
		}
		catch (NoSuchAlgorithmException e) 
		{ e.printStackTrace(); }
        
        return byteDigest;
	}
	
    public static void main(String[] args) throws Exception
    {
    	//internetExample();
		myExample();
    }
	
    public static void myExample() throws Exception
    {
    	String password = "123456";
    	String digest = getMsgDigest(password);
    	
    	System.out.println(digest);
    	System.out.println(digest.length());
    }
    
	public static void internetExample() throws Exception
	{
    	String password = "123456";
    	 
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        md.update(password.getBytes());
 
        byte byteData[] = md.digest();            
       
        //String c = new Base64().encodeAsString(byteData);
        

        
        for(int i = 0; i < byteData.length; i++)
        	System.out.println();
        String key = new String(byteData, 0, byteData.length, "ASCII");
        //String key = new String(byteData);
        //String key = new String(byteData, "UTF8");;
        //String key = bytesToStringUTFCustom(byteData);
        System.out.println(key);
        
        key = "";
        
        for(int i = 0; i < byteData.length; i++)
        {
        	char curChar = (char)byteData[i];
        	key += "" + curChar;
        }
        System.out.println(key);
        
        StringBuilder sb1 = new StringBuilder();
        for (byte b : byteData)
        	sb1.append(String.format("%02X", b));
        
        System.out.println("Hex format : " + sb1.toString());        
        
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        System.out.println("Hex format : " + sb.toString());
 
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	System.out.println("Hex format : " + hexString.toString());
	}
}
