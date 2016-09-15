package mongoose.html.frontend.activities.application;

import com.google.gwt.core.client.EntryPoint;
import mongoose.activities.frontend.application.MongooseFrontendApplication;
import mongoose.activities.frontend.event.fees.FeesActivity;
import mongoose.activities.frontend.event.options.OptionsActivity;
import mongoose.activities.frontend.event.program.ProgramActivity;
import mongoose.activities.frontend.event.terms.TermsActivity;
import mongoose.html.frontend.activities.event.fees.FeesUi;
import mongoose.html.frontend.activities.event.options.OptionsUi;
import mongoose.html.frontend.activities.event.program.ProgramUi;
import mongoose.html.frontend.activities.event.terms.TermsUi;
import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.presentation.PresentationActivity;
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
        PresentationActivity.registerViewBuilder(FeesActivity.class, FeesUi::buildView);
        PresentationActivity.registerViewBuilder(TermsActivity.class, TermsUi::buildView);
        PresentationActivity.registerViewBuilder(ProgramActivity.class, ProgramUi::buildView);
        PresentationActivity.registerViewBuilder(OptionsActivity.class, OptionsUi::buildView);
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
