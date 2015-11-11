package helloworld;

/**
 * @author Bruno Salmon
 */
public class HelloWorldJreApplication {

    public static void main(String[] args) {
        // Calling the application logic
        String helloMessage = HelloLogic.helloMessage();

        // Tracing the message in the console
        System.out.println(helloMessage);
    }
}
