package crypto;

public class Prova1 
{
	public static void main(String args[]) throws InterruptedException
	{
		System.out.println("Prova1");
		SharedProva s = SharedProva.getInstance();
		String key = "BBB";
		s.setDiffieHellmanKey(key);
		System.out.println("Prova1 setta chiave a " + key);
		
		while(true)
		{
			Thread.sleep(5000);
			System.out.println("Prova1 " + s.getDiffieHellmanKey());
		}
	}
}
