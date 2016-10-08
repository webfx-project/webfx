package mongoose.html.frontend.activities.application;

import mongoose.activities.frontend.container.FrontendContainerActivity;
import mongoose.activities.frontend.event.fees.FeesActivity;
import mongoose.activities.frontend.event.options.OptionsActivity;
import mongoose.activities.frontend.event.program.ProgramActivity;
import mongoose.activities.frontend.event.terms.TermsActivity;
import mongoose.html.frontend.activities.container.ContainerUi;
import mongoose.html.frontend.activities.event.fees.HtmlFeesViewModelBuilder;
import mongoose.html.frontend.activities.event.options.OptionsUi;
import mongoose.html.frontend.activities.event.program.HtmlProgramViewModelBuilder;
import mongoose.html.frontend.activities.event.terms.HtmlTermsViewModelBuilder;
import mongoose.html.frontend.activities.highlevelcomponents.HtmlHighLevelComponentsFactory;
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
        HtmlHighLevelComponentsFactory.register();
        PresentationActivity.registerViewBuilder(FrontendContainerActivity.class, ContainerUi::buildView);
        PresentationActivity.registerViewBuilder(FeesActivity.class, new HtmlFeesViewModelBuilder());
        PresentationActivity.registerViewBuilder(TermsActivity.class, new HtmlTermsViewModelBuilder());
        PresentationActivity.registerViewBuilder(ProgramActivity.class, new HtmlProgramViewModelBuilder());
        PresentationActivity.registerViewBuilder(OptionsActivity.class, OptionsUi::buildView);
    }
}
