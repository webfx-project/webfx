package mongoose.client.backend.teavm;

import mongoose.client.backend.MongooseBackendApplication;
import naga.platform.spi.Platform;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendTeaVmApplication {

    /* No need for TeaVmPlatform.register(); as the platform will be found by the ServiceLoader */

    public static void main(String[] args) {
        Platform.setWebLogger(MongooseBackendTeaVmApplication::displayMessageInDOM);
        MongooseBackendApplication.main(args);
    }

    private static void displayMessageInDOM(String message) {
        // Displaying the message in the DOM
        HTMLDocument document = HTMLDocument.current();
        if (splashRunning) {
            document.getBody().clear();
            splashRunning = false;
        }
        HTMLElement node;
        if (message.length() >= 2 && message.charAt(1) == ')')
            node = document.createElement("h2");
        else if (message.startsWith("Generated query mapping:") || message.startsWith("Entities:") || message.startsWith("Display result:") || message.startsWith("["))
            node = document.createElement("pre");
        else
            node = document.createElement("p");
        HTMLElement spanElement = document.createElement("span");
        spanElement.setInnerHTML(message.replaceAll("Done in ", "Done in <b>").replaceAll("ms", "</b>ms"));
        node.appendChild(spanElement);
        document.getBody().appendChild(node);
    }

    private static boolean splashRunning = true;
}
