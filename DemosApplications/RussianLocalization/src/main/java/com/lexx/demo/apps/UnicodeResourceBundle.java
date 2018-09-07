package com.lexx.demo.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class UnicodeResourceBundle extends ResourceBundle {
	
	private HashMap<String, String> properties;
	
	public UnicodeResourceBundle(Reader bis) throws IOException {
		properties = new HashMap<String, String>();
		BufferedReader buf = new BufferedReader(bis);
		String line = null;
		while ((line = buf.readLine()) != null) {
			String a[] = line.split("=");
			String key = a[0].trim();
			String value = a[1].trim();
			properties.put(key, value);
		//	System.out.println(key + "="+ new String (value.getBytes(), Charset.forName("UTF-8")));
		}
		
		Iterator it = properties.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
	//		System.out.println(pairs.getKey() + " + " + pairs.getValue());
		}

			

	}

	@Override
	public Enumeration<String> getKeys() {
		return Collections.enumeration(properties.keySet());
	}

	@Override
	protected Object handleGetObject(String key) {
		return properties.get(key);
	}

}
