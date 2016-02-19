/**
 * 
 */
package org.avinash.fhirclientjs;

import java.util.Arrays;

import org.avinash.fhirclientjs.util.ApplicationContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This is the main class for running the Spring boot application
 * 
 * @author ashanbhag
 *
 */


@EnableAutoConfiguration
@Configuration
@ComponentScan
public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		
		// Lets inspect the beans
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
            
        }
        
        // setting context to ApplicationContextUtils so that the context can 
        // be accessed in the application.
        //System.out.println("Setting ctx to ApplicationContextUtils");
        ApplicationContextUtils appCtxUtils = new ApplicationContextUtils();
        appCtxUtils.setApplicationContext(ctx);
		
	}
	
}
