package servlet;

import crypto.SharedProva;

public class Prova3 
{
	public static void main(String args[]) throws InterruptedException
	{
		System.out.println("Prova3");
		SharedProva s = SharedProva.getInstance();
		System.out.println("Prova3 " + s.getDiffieHellmanKey());		
	}	
}
