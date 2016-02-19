package org.avinash.fhirclientjs.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextUtils implements ApplicationContextAware 
{
	private static ApplicationContext ctx;

	public ApplicationContextUtils() 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException 
	{
		//System.out.println("Inside ApplicationContext. Seting context : " + applicationContext );
		ctx = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() 
	{
		return ctx;
	}
	

}
