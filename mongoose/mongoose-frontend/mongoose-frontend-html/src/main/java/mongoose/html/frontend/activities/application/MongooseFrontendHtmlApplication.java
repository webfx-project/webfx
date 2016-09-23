package mongoose.html.frontend.activities.application;

import mongoose.activities.frontend.container.FrontendContainerActivity;
import mongoose.activities.frontend.event.fees.FeesActivity;
import mongoose.activities.frontend.event.options.OptionsActivity;
import mongoose.activities.frontend.event.program.ProgramActivity;
import mongoose.activities.frontend.event.terms.TermsActivity;
import mongoose.html.frontend.activities.container.ContainerUi;
import mongoose.html.frontend.activities.event.fees.FeesUi;
import mongoose.html.frontend.activities.event.options.OptionsUi;
import mongoose.html.frontend.activities.event.program.ProgramUi;
import mongoose.html.frontend.activities.event.terms.TermsUi;
import mongoose.web.activities.frontend.application.MongooseFrontendWebApplication;
import naga.framework.ui.presentation.PresentationActivity;
import naga.providers.platform.client.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendHtmlApplication extends MongooseFrontendWebApplication {

    protected void registerResourceBundles() {
        GwtPlatform.registerBundle(MongooseFrontendHtmlBundle.B);
    }

    @Override
    protected void registerCustomViewBuilders() {
        PresentationActivity.registerViewBuilder(FrontendContainerActivity.class, ContainerUi::buildView);
        PresentationActivity.registerViewBuilder(FeesActivity.class, FeesUi::buildView);
        PresentationActivity.registerViewBuilder(TermsActivity.class, TermsUi::buildView);
        PresentationActivity.registerViewBuilder(ProgramActivity.class, ProgramUi::buildView);
        PresentationActivity.registerViewBuilder(OptionsActivity.class, OptionsUi::buildView);
    }
}
