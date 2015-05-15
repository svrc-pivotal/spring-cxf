package demo.spring.service;

import java.util.HashMap;
import java.net.URL;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class Application extends SpringBootServletInitializer {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        /*if(System.getProperty("java.security.auth.login.config") == null) {
            String jaasConfigFile = null;
            URL jaasConfigURL = Application.class.getClassLoader().getResource("jaas.conf");
            if(jaasConfigURL != null) {
                jaasConfigFile = jaasConfigURL.getFile();
            }
            System.setProperty("java.security.auth.login.config", jaasConfigFile);
        }
        */
        SpringApplication.run(Application.class, args);
    }

    // Replaces the need for web.xml
    @Bean
    public ServletRegistrationBean servletRegistrationBean(ApplicationContext context) {
        return new ServletRegistrationBean(new CXFServlet(), "/api/*");
    }

    // Replaces cxf-servlet.xml
    @Bean
    // <jaxws:endpoint id="helloWorld" implementor="demo.spring.service.HelloWorldImpl" address="/HelloWorld"/>
    public EndpointImpl helloService() {
        Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);
        Object implementor = new HelloWorldImpl();
        EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        HashMap<String,Object> props = new HashMap<String,Object>();
        props.put("ws-security.username", "bob");
        props.put("ws-security.callback-handler", new KeystorePasswordCallback());
        props.put("ws-security.signature.properties", "/bob.properties");
        props.put("ws-security.subject.cert.constraints", ".*O=apache.org.*");
        props.put("ws-security.encryption.username", "useReqSigCert");
        props.put("ws-security.enable.unsigned-saml-assertion.principal", true);
        // props.put("ws-security.saml1.validator", new CustomSaml2Validator());    
        // props.put("ws-security.saml2.validator", new CustomSaml2Validator());
        endpoint.setWsdlLocation("classpath:wsdl/hello.wsdl");
        endpoint.setProperties(props);
        endpoint.publish("/hello");
        endpoint.getServer().getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
        endpoint.getServer().getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
        
        return endpoint;
    }

    // Configure the embedded tomcat to use same settings as default standalone tomcat deploy
    @Bean
    public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
        // Made to match the context path when deploying to standalone tomcat- can easily be kept in sync w/ properties
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory("/ws-server-1.0", 8080);
        return factory;
    }

    // Used when deploying to a standalone servlet container, i.e. tomcat
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}