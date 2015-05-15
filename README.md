# spring-cxf

Demonstrates ability to host a JAX-WS service using Spring Boot and Apache CXF with zero XML configuration.
Demonstrates SAML Sender-Vouches assertions

## Run the Client and Service in Cloud Foundry
Modify the `serverHost` entry inside the application Manifest `manifest.yml` to reflect your CF domain
In a terminal, execute: `./gradlew assemble; cf push`

Alternatively can be run in your IDE of choice by running `demo.spring.service.Application.java` as a normal Java application.

Alternatively either client or server can be run via executing the `jar` directly:
    export serverHost="localhost:9090"
    java -jar ws-client/build/libs/ws-client-1.0.jar --server.port=7070
    java -jar ws-server/build/libs/ws-server-1.0.jar --server.port=9090

### Service endpoint
The service endpoint is `<context-path>/api/hello`.  The full path to the WSDL/service is:

    http://localhost:8080/ws-server-1.0/api/hello?wsdl

## Run the client
Running the given class `demo.service.client.ClientDemo` demonstrates ability to interact with the service without tight coupling or hard dependencies.  

Running the client will print the following statement showing it received a response from the server:

    11:32:18.394 [main] INFO  demo.service.client.ClientDemo - Response from server: Hello hi, you beautiful world!
 
Alternatively the service can be hit with a testing tool, such as SoapUI or Postman, or any other technology capable of sending text over http.


## Generated client 
The client code was generated via the WSDL.  To reproduce this, in `build.gradle`, set the property `wsdl2java.enabled = ` to `true`, then execute:

    ./gradlew ws-client:wsdl2java
    
This will output the compiled classes in `ws-client/generatedsources`, which can then be used to hit a running service without any coupling or hard dependencies.  This generation only needs to occur when the service interface changes.

