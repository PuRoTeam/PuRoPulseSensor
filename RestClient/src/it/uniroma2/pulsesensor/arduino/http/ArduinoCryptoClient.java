package it.uniroma2.pulsesensor.arduino.http;

import it.uniroma2.pulsesensor.arduino.crypto.ArduinoKeyExchange;
import it.uniroma2.pulsesensor.arduino.crypto.HostData;
import it.uniroma2.pulsesensor.crypto.AES;
import it.uniroma2.pulsesensor.crypto.KeyExchangeData;

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

//Invio di punti criptati
public class ArduinoCryptoClient implements Runnable
{
	public static final String url = HostData.url;
	public boolean runInfiniteTimes = true;
	public int msBetweenRequest = 20;
	
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
	
	//Di quanti giorni vuoi deferire la data. Per test sul grafico replay e date picker
	public long deferDate(int dayToDefer)
	{
		long millisecToDefer = dayToDefer*24*60*60*1000;
		return millisecToDefer;
	}
	
	//invio un array di punti criptato
	public void multiCryptoValue(String key)
			throws ClientProtocolException, IOException	
	{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
      
    	int max = 530; //massimo valore estraibile casualmente
    	int min = 500;
    	int size = 1; //numero di punti da inviare in ogni richiesta POST
    	
    	ArrayList<Double> random = new ArrayList<Double>();
    	for(int i = 0; i < size; i++)
    		random.add(Math.random()*(max-min) + min);
    	
    	int dayToDefer = 0; //cambiare per fare prove su replay in diversi intervalli
    	
    	long uid = 2;
    	
    	String plainJson = "[";
    	for(int i = 0; i < size - 1; i++)
    	{
    		long curTimestamp = System.currentTimeMillis() + deferDate(dayToDefer);    		
    		plainJson += "{\"uid\":" + uid + ",\"timestamp\":" + curTimestamp + ",\"value\":" + random.get(i) +"}, ";
    	}
    	long curTimestamp = System.currentTimeMillis() + deferDate(dayToDefer);
    	plainJson += "{\"uid\":" + uid + ",\"timestamp\":" + curTimestamp + ",\"value\":" + random.get(size - 1) +"}";
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
    		int port = KeyExchangeData.port;
    		long primitive_root = KeyExchangeData.primitive_root;
    		long prime = KeyExchangeData.prime;
    		int keyLength = KeyExchangeData.keyLenghtInBytes; //32 -> 32*8 = 256 bit
    		    		
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
			
			while(true)
			{
				Post(diffieHellmanKey);
				Thread.sleep(msBetweenRequest);
				
				if(!runInfiniteTimes)
				{
					System.out.println("Attenzione, \"runInfiniteTimes\" disabilitato -> solo un'iterazione");
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
