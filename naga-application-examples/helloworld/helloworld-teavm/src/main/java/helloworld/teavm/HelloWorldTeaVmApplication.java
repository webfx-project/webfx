package helloworld.teavm;

import helloworld.HelloWorldLogic;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * @author Bruno Salmon
 */
public class HelloWorldTeaVmApplication {

    public static void main(String[] args) {
        // Calling the application logic
        String helloMessage = HelloWorldLogic.helloMessage();

        // Displaying the message
        displayMessage(helloMessage);
    }

    private static void displayMessage(String helloMessage) {
        // Tracing the message in the console
        System.out.println(helloMessage);

        // Displaying the message in the DOM
        HTMLDocument document = HTMLDocument.current();
        HTMLElement h1 = document.createElement("h1");
        h1.appendChild(document.createTextNode(helloMessage));
        document.getBody().appendChild(h1);
    }

}
