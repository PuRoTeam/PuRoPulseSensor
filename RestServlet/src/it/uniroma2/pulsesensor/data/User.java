package it.uniroma2.pulsesensor.data;

import it.uniroma2.pulsesensor.crypto.SHA256;

public class User 
{
	private String userName;
	private String hashOfPassword;
	private String firstName;
	private String lastName;
	
	public User(String firstName, String lastName, String userName, String password)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.hashOfPassword = SHA256.getMsgDigest(password);
	}
	
	public String getFirstName()
	{
		return firstName;		
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getHashOfPassword()
	{
		return hashOfPassword;
	}
	
    public static void main(String[] args) throws Exception
    {	
    	User a = new User("Claudio", "Pupparo", "MisterPup", "prova");
    	System.out.println(a.getHashOfPassword());
    	System.out.println(a.getHashOfPassword().length());
    }	
}
