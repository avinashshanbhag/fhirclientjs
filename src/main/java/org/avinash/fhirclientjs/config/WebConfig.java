/**
 * 
 */
package org.avinash.fhirclientjs.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @author ashanbhag
 *
 */
public class WebConfig extends WebMvcConfigurerAdapter 
{

	
	@Controller
    static class Routes {
	@RequestMapping({
	    "/",
	    "/all",
	    "/details",
	    "/about",
	    "/login",
	    "/launch"
	})
		public String index() {
			System.out.println("inside Routes");
		    return "forward:/index.html";
		}
    }

}
