package com.quangnam.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;

public class UppercaseConvertMediator extends AbstractMediator { 

	private String name;
	Log log = LogFactory.getLog(UppercaseConvertMediator.class);
	
	public boolean mediate(MessageContext context) { 
		convertToUpperCase(context);
		return true;
	}
	
	private void convertToUpperCase(MessageContext context) {
		String city = (String) context.getProperty("CITY");
		log.debug("Values obtained from sequense is " + city);
		
		String converted = city.toUpperCase();
		
		context.setProperty("CONVERTED", name + " is living at " + converted);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
