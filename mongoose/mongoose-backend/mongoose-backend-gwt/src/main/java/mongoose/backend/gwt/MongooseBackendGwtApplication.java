package mongoose.backend.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.SpanElement;
import mongoose.logic.MongooseLogic;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        registerResourceBundles();
        Platform.setWebLogger(MongooseBackendGwtApplication::displayMessageInDOM);
        MongooseLogic.runBackendApplication();
    }

    private static void registerResourceBundles() {
        GwtPlatform.registerBundle(MongooseBackendGwtBundle.B);
    }

    private static void displayMessageInDOM(String message) {
        // Displaying the message in the DOM
        Document document = Document.get();
        Node node;
        if (message.charAt(1) == ')')
            node = document.createHElement(2);
        else if (message.startsWith("Generated query mapping:") || message.startsWith("Entities:") || message.startsWith("Display result:") || message.startsWith("["))
            node = document.createPreElement();
        else
            node = document.createPElement();
        SpanElement spanElement = document.createSpanElement();
        spanElement.setInnerHTML(message.replaceAll("Done in ", "Done in <b>").replaceAll("ms", "</b>ms"));
        node.appendChild(spanElement);
        document.getBody().appendChild(node);
        if (splashRunning) {
            replaceSplashScreen(document.createBRElement());
            splashRunning = false;
        }
    }

    private static boolean splashRunning = true;

    private static native void replaceSplashScreen(Element e) /*-{ var preloader = $wnd.document.getElementById("preloader"); preloader.innerHTML = ""; preloader.appendChild(e); }-*/;


}
