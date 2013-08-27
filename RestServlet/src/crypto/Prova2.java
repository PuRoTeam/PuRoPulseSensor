package crypto;

public class Prova2 
{
	public static void main(String args[]) throws InterruptedException
	{
		System.out.println("Prova2");
		SharedProva s = SharedProva.getInstance();
		System.out.println("Prova2 " + s.getDiffieHellmanKey());		
	}	
}
