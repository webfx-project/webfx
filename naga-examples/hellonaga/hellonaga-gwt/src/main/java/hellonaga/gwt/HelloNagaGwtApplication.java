package hellonaga.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import hellonaga.HelloNagaLogic;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        new HelloNagaLogic(HelloNagaGwtApplication::displayMessage).run();
    }

    private static void displayMessage(String message) {
        // Tracing the message in the console
        Platform.log(message);

        // Displaying the message in the DOM
        Document document = Document.get();
        HeadingElement p = document.createHElement(2);
        p.setInnerText(message);
        replaceSplashScreen(p);
    }

    private static native void replaceSplashScreen(Element e) /*-{ var preloader = $wnd.document.getElementById("preloader"); preloader.innerHTML = ""; preloader.appendChild(e); }-*/;


}
