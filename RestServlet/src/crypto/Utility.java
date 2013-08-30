package crypto;

public class Utility 
{
    public static byte[] hexStringToByteArray(String s) 
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
	
	public static String bytesToString(byte a[])
	{
		String s = "";
		for(int i = 0; i < a.length; i++)		
			s += (char)a[i];		
		return s;
	}

	public static byte[] stringToBytes(String s)
	{
		byte[] a = new byte[s.length()];
		for(int i = 0; i < s.length(); i++)		
			a[i] = (byte)s.charAt(i);
		return a;
	}
}
