package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import servlet.Shared;

public class KeyExchanger implements Runnable
{
	private Socket clientSocket; 
	private long primitive_root;
	private long prime;
	private int keyLength;	
	private String diffieHellmanKey;
	
	public KeyExchanger(Socket clientSocket, long primitive_root, long prime, int keyLength)
	{
		this.clientSocket = clientSocket;
		this.primitive_root = primitive_root;
		this.prime = prime;
		this.keyLength = keyLength;		
		diffieHellmanKey = "";
	}

	//Lancia thread per ogni connessione accettata. Ogni connessione è uno scambio Diffie Hellman
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException
	{
		int port = 1600;
		long primitive_root = 5;
		long prime = 25657L;
		int keyLength = 32; //32 -> 32*8 = 256 bit
		
		ServerSocket serverSocket = null; //TCP
		InetAddress bindTo = null; //accetta connessioni da chiunque
	
		serverSocket = new ServerSocket(port, 0, bindTo);
		
		while(true)
		{
			System.out.println("In attesa di connessioni");
	        Socket clientSocket = serverSocket.accept(); //bloccante
			System.out.println("Connessione accettata!");
			
			KeyExchanger newKeyExchanger = new KeyExchanger(clientSocket, primitive_root, prime, keyLength);
			
			Thread thread = new Thread(newKeyExchanger); //thread che serve la richiesta
			thread.start();		
		}
	}
	
	public void run() 
	{
		try
		{
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	                 
	        String startExchangeMsg = in.readLine(); //controllo su errori
	        System.out.println(startExchangeMsg);
	        //String ok = "OK";
	        //out.println(ok);
	        
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
	        
	        diffieHellmanKey = key;
	        Shared singleton = Shared.getInstance();
	        singleton.setDiffieHellmanKey(diffieHellmanKey);
	        System.out.println(key);
	        
	        String endExchangeMsg = in.readLine(); //controllo su errori
	        System.out.println(endExchangeMsg);
	        
	        clientSocket.close();
		}
		catch(IOException e)
		{ e.printStackTrace(); }
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
	
	public String getDiffieHellmanKey()
	{
		return diffieHellmanKey;
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
