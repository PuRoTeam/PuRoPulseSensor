package servlet;

public class Prova
{
    public static String avoidInjection(String parameter)
    {
    	String escapedString = parameter;
    	escapedString = parameter.replaceAll("'", "\\\\'");
    	escapedString = escapedString.replaceAll("#", "");
    	escapedString = escapedString.replaceAll("--", "");
    	return escapedString;
    }
	
	public static void main(String[] args)
	{
		String c = "SELECT * FROM user WHERE username='' OR '1'='1' -- AND password='a'";
		c = avoidInjection(c);
		System.out.println(c);
		
		System.out.println(System.getProperty("user.home"));
		
		/*ArrayList<String> a = new ArrayList<String>();
		a.add("claudio");
		a.add("c");
		String[] b = {"a"};
		b = a.toArray(b);
		for (int i = 0; i < b.length; i++)
			System.out.println(b[i]);*/
	}
}
