package hellonaga.teavm;

import hellonaga.HelloNagaLogic;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * @author Bruno Salmon
 */
public class HelloNagaTeaVmApplication {

    /* No need for TeaVmPlatform.register(); as the platform will be found by the ServiceLoader */

    public static void main(String[] args) {
        new HelloNagaLogic(HelloNagaTeaVmApplication::displayMessage).run();
    }

    private static void displayMessage(String helloMessage) {
        // Tracing the message in the console
        System.out.println(helloMessage);

        // Displaying the message in the DOM
        HTMLDocument document = HTMLDocument.current();
        HTMLElement preloader = document.getElementById("preloader");
        preloader.clear();
        HTMLElement p = document.createElement("h2");
        p.appendChild(document.createTextNode(helloMessage));
        preloader.appendChild(p);
    }

}
