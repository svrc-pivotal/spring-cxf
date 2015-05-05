package demo.service.client;

import org.springframework.beans.factory.annotation.Autowired;

import demo.spring.service.HelloWorld;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class ClientController {

  @Autowired
  HelloWorld client;
  
  @RequestMapping("/")
	public String getView() {

        String response = client.sayHi("Hello beautiful world!");
        System.out.println("Response from server: " + response);        
		    return response;
	}
	
}

