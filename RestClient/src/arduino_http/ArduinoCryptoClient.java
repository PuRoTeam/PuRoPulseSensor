package arduino_http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import crypto.AES;
import arduino_crypto.ArduinoKeyExchange;

//Invio di punti cruptati
public class ArduinoCryptoClient implements Runnable
{
	public static final String url = "http://localhost:8080/RestServlet/index.html";
	public boolean runInfiniteTimes = true;
	
	int port;
	long primitive_root;
	long prime;
	int keyLength;
	
	public ArduinoCryptoClient(int port, long primitive_root, long prime, int keyLength)
	{
		this.port = port;
		this.primitive_root = primitive_root;
		this.prime = prime;
		this.keyLength = keyLength;
	}
	
	public String ExchangeKey()
			throws ClientProtocolException, UnknownHostException, IOException 
	{		
		ArduinoKeyExchange clientKeyEx = new ArduinoKeyExchange(port, primitive_root, prime, keyLength);
		String dhKey = clientKeyEx.exchange();
		
		return dhKey;
	}
	
	public void Post(String diffieHellmanKey) 
			throws ClientProtocolException, UnknownHostException, IOException 
	{
		multiCryptoValue(diffieHellmanKey);
	}
	
	//invio un array di punti criptato
	public void multiCryptoValue(String key)
			throws ClientProtocolException, IOException	
	{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
      
    	int max = 100; //massimo valore estraibile casualmente
    	int size = 1; //numero di punti da inviare in ogni richiesta POST
    	
    	ArrayList<Double> random = new ArrayList<Double>();
    	for(int i = 0; i < size; i++)
    		random.add(Math.random()*max);
    	
    	String plainJson = "[";
    	for(int i = 0; i < size - 1; i++)
    		plainJson += "{\"uid\":1,\"timestamp\":" + System.currentTimeMillis() + ",\"value\":" + random.get(i) +"}, ";
    	plainJson += "{\"uid\":1,\"timestamp\":" + System.currentTimeMillis() + ",\"value\":" + random.get(size - 1) +"}";
    	plainJson += "]"; 
    	    	
        //array di punti
        /*String json = "[{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random1 +"}," +
        			   "{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random2 + "}," +
        			   "{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random3 + "}," +
        		      "]";*/
    	
    	try
    	{    		
    		String cryptoJson = AES.EncryptIVFromKey(plainJson, key);
    		
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();            
            params.add(new BasicNameValuePair("JSON", cryptoJson));    
            
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = client.execute(post);
            post.setHeader("Content-Type", "application/json");
                    
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                   
            String line = "";
            while ((line = rd.readLine()) != null) 
            {
                System.out.println(line);
            }
    	}
    	catch(Exception e)
    	{ e.printStackTrace(); }
	}
	
	public void Get() 
			throws ClientProtocolException, IOException
	{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) 
        {
          System.out.println(line);
        }
	}
	
    public static void main(String[] args)
    {
		if(Security.getProvider(AES.providerName) == null);
			Security.addProvider(AES.provider);
			
    	try
    	{
    		int port = 1600;
    		long primitive_root = 5;
    		long prime = 25657L;
    		int keyLength = 32; //32 -> 32*8 = 256 bit
    		    		
    		ArduinoCryptoClient cryptoClient = new ArduinoCryptoClient(port, primitive_root, prime, keyLength);
    		Thread thread = new Thread(cryptoClient); //un solo thread che invia richieste in serie
    		thread.start();
    	}
    	catch(Exception e)
    	{ e.printStackTrace(); }
    }

	public void run() 
	{
		try 
		{
			String diffieHellmanKey = ExchangeKey();
				
			System.out.println("Lunghezza Chiave: " + diffieHellmanKey.length());
			while(true)
			{
				Post(diffieHellmanKey);
				Thread.sleep(150);
				
				if(!runInfiniteTimes)
				{
					System.out.println("Occhio, \"runInfiniteTimes\" disabilitato -> solo un'iterazione");
					break;
				}
			}			
		} 
		catch (InterruptedException e) 
		{ e.printStackTrace(); } 
		catch (ClientProtocolException e) 
		{ e.printStackTrace(); } 
		catch(UnknownHostException e)
		{ e.printStackTrace(); }
		catch (IOException e) 
		{ e.printStackTrace(); }		
	}
}
