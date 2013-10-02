package arduino_crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

import crypto.KeyExchangeData;
import crypto.SHA256;

//esegue solo diffie hellman
public class ArduinoKeyExchange 
{
	private int port; 
	private long primitive_root;
	private long prime;
	private int keyLenght;
	private String diffieHellmanKey;	
	public static final String host = "localhost";
	
	public ArduinoKeyExchange(int port, long primitive_root, long prime, int keyLenght)
	{
		this.port = port;
		this.primitive_root = primitive_root;
		this.prime = prime;
		this.keyLenght = keyLenght;
		diffieHellmanKey = "";
	}
	
	public static void main(String[] args)
    {
		int port = KeyExchangeData.port;
		long primitive_root = KeyExchangeData.primitive_root;
		long prime = KeyExchangeData.prime;
		int keyLength = KeyExchangeData.keyLenght; //32 -> 32*8 = 256 bit
		
		ArduinoKeyExchange clientKeyEx = new ArduinoKeyExchange(port, primitive_root, prime, keyLength);
		try 
		{
			clientKeyEx.exchange();
		} 
		catch (IOException e) 
		{ e.printStackTrace(); }
    }

	public String exchange() throws UnknownHostException, IOException
	{
		Socket clientSocket = null;
		
		PrintWriter out = null;
        BufferedReader in = null;
	
		clientSocket = new Socket(host, port);
		
		System.out.println("Connessione instaurata!");			
		
		out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 
        out.println("ciao");

        //String key = iterativeExchange(in, out);  //32 scambi diffie hellman
        String key = sha256Exchange(in, out); //una sola iterazione, hash chiave con sha256
        
        diffieHellmanKey = key;
        
        System.out.println(key);
        
        out.println("fine");
        
        clientSocket.close();
        
        return diffieHellmanKey;
	}
	
	//un solo scambio e poi hash con sha256
	public String sha256Exchange(BufferedReader in, PrintWriter out) throws IOException 
	{		
    	long x = (long)(Math.random()*(prime - 2) + 2); //[2, p-1]  chiave privata iterazione corrente
    	BigInteger base = new BigInteger(String.valueOf(primitive_root)); //g
    	BigInteger exp = new BigInteger(String.valueOf(x));
    	BigInteger modPrime = new BigInteger(String.valueOf(prime)); //n   
    	BigInteger ret = base.modPow(exp, modPrime); //(g^x) mod n
    	
    	out.println(ret); //invio    	
    	String newKeyChar = in.readLine(); //lettura bloccante    	
    	
    	BigInteger newKeyInt = new BigInteger(newKeyChar); //(g^y) mod n
    	BigInteger newKeyLong = newKeyInt.modPow(exp, modPrime); //((g^y)^x) mod n   
    	String key = newKeyLong.toString();    	
    	String msgDigest = SHA256.getMsgDigest(key);
    	
    	return msgDigest;
	}
	
	//32 scambi
	public String iterativeExchange(BufferedReader in, PrintWriter out) throws IOException
	{
		String key = "";
		
        for(int i = 0; i < keyLenght; i++)
        {
        	/*----Prima scrivo poi leggo----*/
        	
        	long x = (long)(Math.random()*(prime - 2) + 2); //[2, p-1]  chiave privata iterazione corrente
        	BigInteger base = new BigInteger(String.valueOf(primitive_root)); //g
        	BigInteger exp = new BigInteger(String.valueOf(x));
        	BigInteger modPrime = new BigInteger(String.valueOf(prime)); //n   
        	BigInteger ret = base.modPow(exp, modPrime); //(g^x) mod n
        	
        	System.out.println("Scrittura di " + ret);
        	out.println(ret); //invio
        	
        	/*---Lettura---*/
        	
        	String newKeyChar = in.readLine(); //lettura bloccante
        	System.out.println("Leggo Y altro: "+newKeyChar);
        	
        	BigInteger newKeyInt = new BigInteger(newKeyChar); //(g^y) mod n
        	BigInteger newKeyLong = newKeyInt.modPow(exp, modPrime); //((g^y)^x) mod n
        	
        	BigInteger modChar = new BigInteger(String.valueOf(256));
        	BigInteger newKeyLongMod = newKeyLong.mod(modChar); //((g^xy) mod n) mod 256 - trasformo in carattere
        	
        	long newLong = newKeyLongMod.longValue();            	
        	char c = (char)newLong; //converto in ascii
        	
        	newKeyChar = "" + c; //conversione easy da char a String            	
        	key += newKeyChar;
        	
        	System.out.println("Prossimo byte chiave: "+newKeyLongMod);            	
        }
        
        return key;
	}
	
	public int getPort()
	{
		return port;
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
		return keyLenght;
	}
	
	public String getDiffieHellmanKey()
	{
		return diffieHellmanKey;
	}
	
	public void setPort(int port)
	{
		this.port = port;
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
		this.keyLenght = keyLength;
	}
}
