package mongoose.client.backend.html;

import com.google.gwt.core.client.EntryPoint;
import mongoose.client.backend.MongooseBackendApplication;
import naga.framework.activity.client.UiApplicationContext;
import naga.providers.platform.client.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendHtmlApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        registerResourceBundles();
        MongooseBackendApplication.main(null);
        UiApplicationContext.onWindowReady(MongooseBackendHtmlApplication::removeSplashScreen);
    }

    private static void registerResourceBundles() {
        GwtPlatform.registerBundle(MongooseBackendGwtBundle.B);
    }

    private static native void removeSplashScreen() /*-{
        var preloader = $wnd.document.getElementById("preloader");
        preloader.parentNode.removeChild(preloader);
    }-*/;

}
