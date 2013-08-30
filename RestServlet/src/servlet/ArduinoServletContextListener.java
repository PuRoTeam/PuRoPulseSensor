package servlet;

import java.security.Security;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import crypto.AES;
import crypto.KeyExchangeManager;
 
//E' un listener che si pone in attesa di eventi legati alla servlet. 
public class ArduinoServletContextListener implements ServletContextListener
{ 	
	private static KeyExchangeManager managerThread = null;
	
	public void contextInitialized(ServletContextEvent arg0) 
	{
		System.out.println("ArduinoServletContextListener started");
	     if ((managerThread == null) || (!managerThread.isAlive())) 
	     {
	 		if(Security.getProvider(AES.providerName) == null);
	 			Security.addProvider(AES.provider);
	    	 
	    	 managerThread = new KeyExchangeManager();
	    	 managerThread.start();
	     }
	}
	
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		System.out.println("ArduinoServletContextListener destroyed");
		
		if (managerThread != null) 		
    	   managerThread.interrupt();        
	}
}