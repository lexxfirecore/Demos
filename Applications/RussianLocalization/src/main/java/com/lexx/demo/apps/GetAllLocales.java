package com.lexx.demo.apps;

import java.util.Locale;

public class GetAllLocales
{
	public static void main(String [] arg)
	{
		Locale [] lst = Locale.getAvailableLocales();
		Locale rus = new Locale("ru", "RU");
		Locale md = new Locale("md", "MD");
		
		for(Locale l: lst)			System.out.print(l+"  |  ");
		System.out.println("");
		for(Locale l: lst)
			if(l.toString().contains("en"))		
				System.out.println(l);
	}
}