package naga.teavm;

/*
 * @author Bruno Salmon
 */

import naga.core.Naga;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class TeaVmApplication {

    public static void main(String[] args) {
        String nagaVersion = new Naga().getVersion();
        // Displaying naga version
        displayMessage(nagaVersion);
    }

    private static void displayMessage(String message) {
        // Tracing the message in the console
        System.out.println(message);

        // Displaying the message in the DOM
        HTMLDocument document = HTMLDocument.current();
        HTMLElement p = document.createElement("p");
        p.appendChild(document.createTextNode(message));
        document.getBody().appendChild(p);
    }
}
