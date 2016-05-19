package mongoose.frontend.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import mongoose.logic.MongooseFrontendApplication;
import naga.core.spi.platform.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        replaceSplashScreen(Document.get().createBRElement());
        registerResourceBundles();
        MongooseFrontendApplication.main(null);
    }

    private static void registerResourceBundles() {
        GwtPlatform.registerBundle(MongooseFrontendGwtBundle.B);
    }

    private static native void replaceSplashScreen(Element e) /*-{ var preloader = $wnd.document.getElementById("preloader"); preloader.innerHTML = ""; preloader.appendChild(e); }-*/;


}
