package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.json.JSONException;
import org.json.JSONObject;

public class Client implements Runnable
{
	public static final String url = "http://localhost:8080/RestServlet/index.html";
	
	public static void Post() 
			throws ClientProtocolException, IOException 
	{
		//singleValue();
		multiValue();
	}
	
	//invio un array di punti
	public static void multiValue()
			throws ClientProtocolException, IOException	
	{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
      
    	int max = 100;
    	int size = 10;
    	
    	ArrayList<Double> random = new ArrayList<Double>();
    	for(int i = 0; i < size; i++)
    		random.add(Math.random()*max);
    	
    	String json = "[";
    	for(int i = 0; i < size - 1; i++)
    		json += "{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random.get(i) +"}, ";
    	json += "{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random.get(size - 1) +"}";
    	json += "]";
    	
    	System.out.println(json);
    	
        //array di punti
        /*String json = "[{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random1 +"}," +
        			   "{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random2 + "}," +
        			   "{\"uid\":1,\"timestamp\":1374256224200,\"value\":" + random3 + "}," +
        		      "]";*/

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("JSON", json.toString()));     
        
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
	
	//invio un singolo punto
	public static void singleValue()
			throws ClientProtocolException, IOException					
	{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        
        JSONObject json = new JSONObject();
        
        int max = 100;
        
    	long uid = 1;
        double value = Math.random()*max;
        long timestamp = System.currentTimeMillis();
        
        try {
			json.put("uid", uid);
			json.put("value", value);
			json.put("timestamp", timestamp);
		} catch (JSONException e) { 
			e.printStackTrace();
		}

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("JSON", json.toString()));     
        
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
	
	public static void Get() 
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
    	try
    	{
    		Client c = new Client();
    		c.run();
    	}
    	catch(Exception e)
    	{ e.printStackTrace(); }
    }

	public void run() {
		boolean keepon = true;
		while(keepon) {
			try {
				Post();
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			keepon = true;
			if(!keepon)
				System.out.println("Occhio, \"keepon\" disabilitato -> solo un'iterazione");
		}
	}
}