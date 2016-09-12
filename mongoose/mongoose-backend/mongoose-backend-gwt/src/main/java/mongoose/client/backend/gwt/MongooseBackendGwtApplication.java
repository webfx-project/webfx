package mongoose.client.backend.gwt;

import com.google.gwt.core.client.EntryPoint;
import mongoose.activities.backend.application.MongooseBackendApplication;
import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.rx.RxUi;
import naga.platform.bus.call.PendingBusCall;
import naga.providers.platform.client.gwt.GwtPlatform;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        registerResourceBundles();
        MongooseBackendApplication.main(null);
        Observable.combineLatest(
                RxUi.observe(UiApplicationContext.getUiApplicationContext().windowBoundProperty()),
                RxUi.observe(PendingBusCall.pendingCallsCountProperty()),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0
        ).subscribe(MongooseBackendGwtApplication::setLoadingSpinnerVisible);

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

    private static native void setLoadingSpinnerVisible(boolean visible) /*-{
        $wnd.document.getElementById("loadingSpinner").setAttribute("style", "visibility: " + (visible ? "visible" : "hidden"));
    }-*/;


}
