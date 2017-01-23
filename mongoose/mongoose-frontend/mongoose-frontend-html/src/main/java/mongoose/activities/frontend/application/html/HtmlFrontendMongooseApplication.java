package mongoose.activities.frontend.application.html;

import mongoose.activities.frontend.application.web.WebFrontendMongooseApplication;
import naga.providers.platform.client.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class HtmlFrontendMongooseApplication extends WebFrontendMongooseApplication {

    protected void registerResourceBundles() {
        GwtPlatform.registerBundle(HtmlFrontendMongooseBundle.B);
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
