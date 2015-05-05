package demo.service.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import demo.spring.service.HelloWorld;


@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Bean
    public HelloWorld helloService() {
    	
    	  JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
    	  factory.setWsdlLocation("classpath:wsdl/hello.wsdl");
        
        factory.setAddress("http://wsserver.yyc.stucharlton.com/ws-server-1.0/api/hello");
        
        factory.setServiceName(QName.valueOf("{http://service.spring.demo/}HelloWorldImplService"));
        factory.setEndpointName(QName.valueOf("{http://service.spring.demo/}HelloWorldImplPort"));
        factory.setServiceClass(HelloWorld.class);
        factory.setUsername("alice");
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("ws-security.callback-handler", new KeystoreCallbackHandler());
        props.put("ws-security.encryption.username", "bob");
        props.put("ws-security.encryption.properties", "bob.properties");
        props.put("ws-security.signature.username", "alice");
        props.put("ws-security.signature.properties", "alice.properties");
        
        props.put("ws-security.saml-callback-handler", new demo.spring.service.SamlCallbackHandler());
        factory.setProperties(props);
        HelloWorld client = (HelloWorld) factory.create();
        return client;
    }
}
