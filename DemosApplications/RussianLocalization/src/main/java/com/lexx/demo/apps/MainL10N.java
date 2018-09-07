package com.lexx.demo.apps;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainL10N {
	
	public static String[] arr= {"HSP66", "DPS22", "MCR101", "IPM-B", "SAT SW", "ASI-MATRIX"};
	
	public static void main(String[] args) throws Exception{
		
	/*	
		System.out.println("getBundle");
		ResourceBundle bundle1 = ResourceBundle.getBundle("com.lexxstudy.localization.Bundle");
		displayValues(bundle1);
	
		System.out.println("getBundle Default");
		Locale defaultLocale = Locale.getDefault();
		ResourceBundle bundle2 = ResourceBundle.getBundle("com.lexxstudy.localization.Bundle", defaultLocale);
		displayValues(bundle2);
	*/	
		System.out.println("getBundle English");
		Locale englishLocale = new Locale("en", "EN");
		ResourceBundle bundle3 = ResourceBundle.getBundle("com.lexxstudy.localization.Bundle", Locale.ENGLISH);
		displayValues(bundle3);
		
	/*	System.out.println("getBundle base RU");
		Locale russianLocale = new Locale("ru", "RU");
		ResourceBundle bundle4 = ResourceBundle.getBundle("com.lexxstudy.localization.Bundle", russianLocale);
		displayValues(bundle4);
	*/
		System.out.println("getBundle override RU");
		Locale russianLocaleU = new Locale("ru", "RU");
		ResourceBundle bundle5 = UnicodeResourceBundle.getBundle("com.lexxstudy.localization.Bundle", russianLocaleU, new UnicodeBundleControl());
		displayValues(bundle5);
		

	}
	
	
	
	
	public static void displayValues(ResourceBundle bundle){
		System.out.println(bundle.getString("my.hello"));
		System.out.println(MessageFormat.format(bundle.getString(Messages.MY_HELLO), (Object[]) arr));
		System.out.println(MessageFormat.format(bundle.getString(Messages.MY_HELLO), (Object[]) arr));
		System.out.println(MessageFormat.format(bundle.getString(Messages.MY_HELLO), (Object[]) arr));

	}
}
