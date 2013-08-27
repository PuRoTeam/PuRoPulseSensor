package crypto;

public class SharedProva 
{
    private static SharedProva instance = null;
    private String diffieHellmanKey = "";
    
    private SharedProva() {}
 
    public static synchronized SharedProva getInstance() 
    {
        if (instance == null) 
        	instance = new SharedProva();
        return instance;
    }
    
    public String getDiffieHellmanKey()
    {
    	return diffieHellmanKey;
    }
    
    public void setDiffieHellmanKey(String diffieHellmanKey)
    {
    	this.diffieHellmanKey = diffieHellmanKey;
    }
}
