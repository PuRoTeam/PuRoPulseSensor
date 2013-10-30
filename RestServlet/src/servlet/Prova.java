package servlet;

import java.util.ArrayList;

public class Prova
{
	public static void main(String[] args)
	{
		ArrayList<String> a = new ArrayList<String>();
		a.add("claudio");
		a.add("c");
		String[] b = {"a"};
		b = a.toArray(b);
		for (int i = 0; i < b.length; i++)
			System.out.println(b[i]);
	}
}
