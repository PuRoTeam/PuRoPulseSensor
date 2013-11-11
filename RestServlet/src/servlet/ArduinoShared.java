package servlet;

public class ArduinoShared 
{
	private String arduinoIP;
	private String diffieHellmanKey;
	private ShareTime shareTime;	
	
	public ArduinoShared(String arduinoIP, String diffieHellmanKey, ShareTime shareTime)
	{
		this.arduinoIP = arduinoIP;
		this.diffieHellmanKey = diffieHellmanKey;
		this.shareTime = shareTime;
	}
	
	public String getArduinoIP()
	{
		return arduinoIP;
	}
	
	public String getDiffieHellmanKey()
	{
		return diffieHellmanKey;
	}
	
	public ShareTime getShareTime()
	{
		return shareTime;
	}
	
	public void setArduinoIP(String arduinoIP)
	{
		this.arduinoIP = arduinoIP;
	}
	
	public void setDiffieHellmanKey(String diffieHellmanKey)
	{
		this.diffieHellmanKey = diffieHellmanKey;
	}
	
	public void setShareTime(ShareTime shareTime)
	{
		this.shareTime = shareTime;
	}
}
