package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import servlet.ArduinoShared;
import servlet.ShareTime;
import servlet.Shared;

public class KeyExchanger implements Runnable
{
	private Socket clientSocket; 
	private long primitive_root;
	private long prime;
	private int keyLength;
	
	public KeyExchanger(Socket clientSocket, long primitive_root, long prime, int keyLength)
	{
		this.clientSocket = clientSocket;
		this.primitive_root = primitive_root;
		this.prime = prime;
		this.keyLength = keyLength;
	}
	
	public void run() 
	{
		try
		{
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
	        //1. Start
	        String startExchangeMsg = in.readLine();
	        System.out.println(startExchangeMsg);
	        
	        //2. Diffie Hellman
	        String diffieHellmanKey = sha256Exchange(in, out); //una sola iterazione, hash chiave con sha256
	        //System.out.println("diffieHellmanKey: " + diffieHellmanKey);
	  
	        /*//3. Authentication
	        String authChallenge = in.readLine(); //se fallisco evito di salvarmi le informazioni su chiave, timestamp, ip
	        	        
	        if(!checkAuthentication(authChallenge, diffieHellmanKey))
	        {
	        	throw new Exception();	        	
	        }*/
	        
	        String clientIP = getClientIP();
	        ArduinoShared newArduinoShared = new ArduinoShared(clientIP, diffieHellmanKey, null); //devo impostare SUBITO la chiave, altrimenti errore perchè parte prima la POST e non trova la chiave
	        Shared.getInstance().addNewArduinoToList(newArduinoShared);	        
	        		
	        //3. Initial Timestamp Agreement
	        String cryptoInitialTimestamp = in.readLine();
	        System.out.println("cryptoInitialTimestamp: " + cryptoInitialTimestamp);
	        String plainInitialTimestamp = AES.DecryptIVFromKey(cryptoInitialTimestamp, diffieHellmanKey);
	        System.out.println("plainInitialTimestamp: " + plainInitialTimestamp);
	    
	        newArduinoShared.setShareTime(new ShareTime(System.currentTimeMillis(), Long.parseLong(plainInitialTimestamp))); //"puntatore" elemento inserito nell'array
	        	        
	        //4. End
	        String endExchangeMsg = in.readLine();
	        System.out.println(endExchangeMsg);		
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		finally
		{
			try
			{ clientSocket.close();	}
			catch(IOException e)
			{ e.printStackTrace(); }
		}
	}
	
	//un solo scambio e poi hash con sha256
	public String sha256Exchange(BufferedReader in, PrintWriter out) throws IOException 
	{		
    	long x = (long)(Math.random()*(prime - 2) + 2); //[2, p-1]  chiave privata iterazione corrente
    	BigInteger base = new BigInteger(String.valueOf(primitive_root)); //g
    	BigInteger exp = new BigInteger(String.valueOf(x));
    	BigInteger modPrime = new BigInteger(String.valueOf(prime)); //n   
    	BigInteger ret = base.modPow(exp, modPrime); //(g^x) mod n
    	
    	String newKeyChar = in.readLine(); //lettura bloccante
    	out.println(ret); //invio
    	    	
    	BigInteger newKeyInt = new BigInteger(newKeyChar); //(g^y) mod n
    	BigInteger newKeyLong = newKeyInt.modPow(exp, modPrime); //((g^y)^x) mod n   
    	String key = newKeyLong.toString();    	
    	String msgDigest = SHA256.getMsgDigest(key);
    	
    	return msgDigest;
	}
	
	public boolean checkAuthentication(String authChallenge, String diffieHellmanKey) 
	{
		try
		{
			AES.DecryptIVFromKey(authChallenge, diffieHellmanKey);
			
			Long solution = new Long(prime - primitive_root);			
			Long toCheck = Long.parseLong(authChallenge);
			
			if(solution == toCheck)
				return true;
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		return false;
	}
	
	//32 scambi
	public String iterativeExchange(BufferedReader in, PrintWriter out) throws IOException 
	{
		String key = ""; //chiave da costruire carattere per carattere
		
        for(int i = 0; i < keyLength; i++)
        {
        	long x = (long)(Math.random()*(prime - 2) + 2); //[2, p-1]  chiave privata iterazione corrente
        	BigInteger base = new BigInteger(String.valueOf(primitive_root)); //g
        	BigInteger exp = new BigInteger(String.valueOf(x));
        	BigInteger modPrime = new BigInteger(String.valueOf(prime)); //n   
        	BigInteger ret = base.modPow(exp, modPrime); //(g^x) mod n
        	
        	/*----Prima leggo poi invio----*/
        	String newKeyChar = in.readLine(); //lettura bloccante
        	System.out.println("Leggo Y altro: "+newKeyChar);
        	System.out.println("Calcolo Y: "+ret+" ed invio");
        	
        	BigInteger newKeyInt = new BigInteger(newKeyChar); //(g^y) mod n
        	BigInteger newKeyLong = newKeyInt.modPow(exp, modPrime); //((g^y)^x) mod n
        	
        	BigInteger modChar = new BigInteger(String.valueOf(256));
        	BigInteger newKeyLongMod = newKeyLong.mod(modChar); //((g^xy) mod n) mod 256 - trasformo in carattere
        	
        	long newLong = newKeyLongMod.longValue();            	
        	char c = (char)newLong; //converto in ascii
        	
        	newKeyChar = "" + c; //conversione easy da char a String            	
        	key += newKeyChar;
        	
        	System.out.println("Prossimo byte chiave: "+newKeyLongMod); 
        	System.out.println("");

        	/*----Dopo aver letto invio----*/
        	out.println(ret); //invio
        }
        
        return key;
	}
	
	public String getClientIP()
	{		
		String clientIP = clientSocket.getInetAddress().getHostAddress().toString();
        System.out.println("KEY EX - Client IP: " + clientIP);
        return clientIP;
	}
	
	public Socket getClientSocket()
	{
		return clientSocket;
	}
	
	public long getPrimitiveRoot()
	{
		return primitive_root;
	}
	
	public long getPrime()
	{
		return prime;
	}
	
	public int getKeyLength()
	{
		return keyLength;
	}
		
	public void setClientSocket(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}
	
	public void setPrimitiveRoot(long primitiveRoot)
	{
		this.primitive_root = primitiveRoot;
	}
	
	public void setPrime(int prime)
	{
		this.prime = prime;
	}
	
	public void setKeyLength(int keyLength)
	{
		this.keyLength = keyLength;
	}
}
