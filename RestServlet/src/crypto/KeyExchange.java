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
			
			Socket clientSocket = serverSocket.accept(); //bloccante
            			
			out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
            System.out.println("In attesa di connessioni");
            
            String startExchange = in.readLine(); //controllo su errori
            System.out.println(startExchange);
            String ok = "OK";
                        
            out.println(ok);
            
            String key = ""; //chiave da costruire carattere per carattere
            
            int keyLenght = 5; //32 -> 32*8 = 256 bit
            
            for(int i = 0; i < keyLenght; i++)
            {
            	String newKeyChar = in.readLine(); //bloccante
            	
            	long newLong = Long.parseLong(newKeyChar);
            	newLong %= 256; //trasformo in carattere
            	
            	char c = (char)newLong; //converto in ascii
            	
            	newKeyChar = "" + c; //conversione easy da char a String            	
            	key += newKeyChar;
            	
            	long x = (long)(Math.random()*(prime - 2) + 2); //[2, p-1]
            	
            	BigInteger base = new BigInteger(String.valueOf(primitive_root));
            	BigInteger exp = new BigInteger(String.valueOf(x));
            	BigInteger mod = new BigInteger(String.valueOf(prime));
            	
            	BigInteger ret = base.modPow(exp, mod);
            	out.println(ret);
            }
            
            System.out.println(key);
            
            String endExchange = in.readLine(); //controllo su errori
            System.out.println(endExchange);
		} 
		catch (IOException e) 
		{ e.printStackTrace(); }
	}
}
