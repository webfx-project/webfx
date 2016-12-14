package mongoose.html.frontend.activities.application;

import mongoose.web.activities.frontend.application.MongooseFrontendWebApplication;
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
/*
        HtmlHighLevelComponentsFactory.register();
        PresentationActivity.registerViewBuilder(FrontendContainerActivity.class, ContainerUi::buildView);
        PresentationActivity.registerViewBuilder(FeesActivity.class, new HtmlFeesViewModelBuilder());
        PresentationActivity.registerViewBuilder(TermsActivity.class, new HtmlTermsViewModelBuilder());
        PresentationActivity.registerViewBuilder(ProgramActivity.class, new HtmlProgramViewModelBuilder());
        PresentationActivity.registerViewBuilder(OptionsActivity.class, new HtmlOptionsViewModelBuilder());
*/
    }
}
