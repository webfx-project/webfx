package hellonaga.teavm;

import hellonaga.HelloNagaLogic;
import naga.core.spi.platform.Platform;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * @author Bruno Salmon
 */
public class HelloNagaTeaVmApplication {

    /* No need for TeaVmPlatform.register(); as the platform will be found by the ServiceLoader */

    public static void main(String[] args) {
        Platform.setWebLogger(HelloNagaTeaVmApplication::displayMessageInDom);
        new HelloNagaLogic().runClient();
    }

    private static void displayMessageInDom(String helloMessage) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement preloader = document.getElementById("preloader");
        preloader.clear();
        HTMLElement p = document.createElement("h2");
        p.appendChild(document.createTextNode(helloMessage));
        preloader.appendChild(p);
    }

}
