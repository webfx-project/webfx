package mongoose.client.backend.gwt;

import com.google.gwt.core.client.EntryPoint;
import mongoose.client.backend.MongooseBackendApplication;
import naga.framework.activity.client.UiApplicationContext;
import naga.providers.platform.client.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        registerResourceBundles();
        MongooseBackendApplication.main(null);
        UiApplicationContext.onWindowReady(MongooseBackendGwtApplication::removeSplashScreen);

        /*
        PresentationActivity.registerViewBuilder(OrganizationsActivity.class, OrganizationsPolymerUi.viewBuilder);
        Polymer.importHref("iron-flex-layout/iron-flex-layout.html", o -> {
            Document doc = Document.get();
            StyleElement styleElement = doc.createStyleElement();
            styleElement.setAttribute("is", "custom-style");
            styleElement.setAttribute("include", "iron-flex iron-flex-alignment");
            doc.getHead().insertFirst(styleElement);
            MongooseBackendApplication.main(null);
            return null;
        });
        */
    }

    private static void registerResourceBundles() {
        GwtPlatform.registerBundle(MongooseBackendGwtBundle.B);
    }

    private static native void removeSplashScreen() /*-{
        var preloader = $wnd.document.getElementById("preloader");
        preloader.parentNode.removeChild(preloader);
    }-*/;


}
