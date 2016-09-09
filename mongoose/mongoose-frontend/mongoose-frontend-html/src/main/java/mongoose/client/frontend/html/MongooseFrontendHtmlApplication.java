package mongoose.client.frontend.html;

import com.google.gwt.core.client.EntryPoint;
import mongoose.client.frontend.MongooseFrontendApplication;
import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.rx.RxUi;
import naga.platform.bus.call.PendingBusCall;
import naga.providers.platform.client.gwt.GwtPlatform;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendHtmlApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        registerResourceBundles();
        MongooseFrontendApplication.main(null);
        Observable.combineLatest(
                RxUi.observe(UiApplicationContext.getUiApplicationContext().windowBoundProperty()),
                RxUi.observe(PendingBusCall.pendingCallsCountProperty()),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0
        ).subscribe(MongooseFrontendHtmlApplication::setLoadingSpinnerVisible);
    }

    private static void registerResourceBundles() {
        GwtPlatform.registerBundle(MongooseFrontendGwtBundle.B);
    }

    private static native void setLoadingSpinnerVisible(boolean visible) /*-{
        var loadingSpinner = $wnd.document.getElementById("loadingSpinner");
        if (!loadingSpinner) {
            if (!visible)
                return;
            loadingSpinner = $wnd.document.createElement("table");
            loadingSpinner.setAttribute("id", "loadingSpinner");
            loadingSpinner.innerHTML = '<tr> <td style="text-align: center; vertical-align: middle;"> <div class="loader"> <svg class="circular"> <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10"></circle> </svg> </div> </td> </tr>';
            $wnd.document.body.insertBefore(loadingSpinner, $wnd.document.body.firstChild);
        }
        loadingSpinner.setAttribute("style", "visibility: " + (visible ? "visible" : "hidden"));
    }-*/;

}
