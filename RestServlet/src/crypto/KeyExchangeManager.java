package crypto;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class KeyExchangeManager extends Thread
{	
	int port = KeyExchangeData.port;
	long primitive_root = KeyExchangeData.primitive_root;
	long prime = KeyExchangeData.prime;
	int keyLength = KeyExchangeData.keyLenghtInBytes; //32 -> 32*8 = 256 bit
	
	public static void main(String[] args)
	{
		KeyExchangeManager managerThread = new KeyExchangeManager();
   	 	managerThread.start();
	}
	
	//KeyExchangeManager è un (unico) thread che si pone in attesa di connessioni.
	//Quando ne riceve una la passa ad un thread KeyExchanger
	public void run() 
	{		
		ServerSocket serverSocket = null; //TCP
		InetAddress bindTo = null; //accetta connessioni da chiunque
		
		try
		{
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
		catch(IOException e)
		{ e.printStackTrace(); }
	}
}
