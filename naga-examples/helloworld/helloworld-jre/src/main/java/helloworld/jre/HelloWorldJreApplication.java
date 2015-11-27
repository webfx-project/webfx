package helloworld.jre;

import helloworld.HelloWorldLogic;

/**
 * @author Bruno Salmon
 */
public class HelloWorldJreApplication {

    public static void main(String[] args) {
        // Calling the application logic
        String helloMessage = HelloWorldLogic.helloMessage();

        // Tracing the message in the console
        System.out.println(helloMessage);
    }
}
