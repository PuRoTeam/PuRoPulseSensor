package it.uniroma2.pulsesensor.arduino.crypto;

public class HostData 
{
	public static final String protocol = "http";	
	public static final String host = "localhost";//"192.168.1.102";
	public static final String port = "8080";
	public static final String path = "/RestServlet/index.html";	
	public static final String url = protocol + "://" + host + ":" + port + path;
}
