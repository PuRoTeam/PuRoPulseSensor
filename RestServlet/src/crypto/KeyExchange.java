package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class KeyExchange
{
	public int port; 
	public long primitive_root;
	public long prime;
	
	public KeyExchange(int port, long primitive_root, long prime)
	{
		this.port = port;
		this.primitive_root = primitive_root;
		this.prime = prime;
	}
	
	public static void main(String[] args)
    {
		int port = 1600;
		long primitive_root = 2;
		long prime = 2147802347L;
		
    	try
    	{
    		KeyExchange keyEx = new KeyExchange(port, primitive_root, prime);
    		keyEx.exchange();
    	}
    	catch(Exception e)
    	{ e.printStackTrace(); }
    }

	public void exchange() 
	{
		ServerSocket serverSocket = null; //TCP
		InetAddress bindTo = null; //accetta connessioni da chiunque
		
		PrintWriter out = null;
        BufferedReader in = null;
		
		try 
		{
			serverSocket = new ServerSocket(port, 0, bindTo);
			
			System.out.println("In attesa di connessioni");
            Socket clientSocket = serverSocket.accept(); //bloccante
			System.out.println("Connessione accettata!");			
			
			out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     
            String startExchange = in.readLine(); //controllo su errori
            System.out.println(startExchange);
            String ok = "OK";
                        
            out.println(ok);
            
            String key = ""; //chiave da costruire carattere per carattere
            
            int keyLenght = 5; //32 -> 32*8 = 256 bit
            
            for(int i = 0; i < keyLenght; i++)
            {
            	long x = (long)(Math.random()*(prime - 2) + 2); //[2, p-1]  chiave privata iterazione corrente
            	
            	BigInteger base = new BigInteger(String.valueOf(primitive_root)); //g
            	BigInteger exp = new BigInteger(String.valueOf(x));
            	BigInteger modPrime = new BigInteger(String.valueOf(prime)); //n   
            	
            	BigInteger ret = base.modPow(exp, modPrime); //(g^x) mod n
            	System.out.println("Y: "+ret);
            	
            	/*----Prima leggo poi invio----*/
            	
            	String newKeyChar = in.readLine(); //lettura bloccante
            	System.out.println("Leggo: "+newKeyChar);
            	
            	BigInteger newKeyInt = new BigInteger(newKeyChar); //(g^y) mod n
            	BigInteger newKeyLong = newKeyInt.modPow(exp, modPrime); //((g^y)^x) mod n
            	
            	BigInteger modChar = new BigInteger(String.valueOf(256));
            	newKeyLong = newKeyLong.mod(modChar); //((g^xy) mod n) mod 256 - trasformo in carattere
            	
            	long newLong = newKeyLong.longValue();            	
            	char c = (char)newLong; //converto in ascii
            	
            	newKeyChar = "" + c; //conversione easy da char a String            	
            	key += newKeyChar;
            	
            	System.out.println("Prossimo byte chiave: "+newKeyChar);            	

            	/*----Dopo aver letto invio----*/
            	
            	out.println(ret); //invio
            }
            
            System.out.println(key);
            
            String endExchange = in.readLine(); //controllo su errori
            System.out.println(endExchange);
		} 
		catch (IOException e) 
		{ e.printStackTrace(); }
	}
}
