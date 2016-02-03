package hellonaga.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import hellonaga.logic.HelloNagaLogic;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        Platform.setWebLogger(HelloNagaGwtApplication::displayMessageInDOM);
        HelloNagaLogic.runClient();
    }

    private static void displayMessageInDOM(String message) {
        Document document = Document.get();
        HeadingElement p = document.createHElement(2);
        p.setInnerText(message);
        replaceSplashScreen(p);
    }

    private static native void replaceSplashScreen(Element e) /*-{ var preloader = $wnd.document.getElementById("preloader"); preloader.innerHTML = ""; preloader.appendChild(e); }-*/;


}
