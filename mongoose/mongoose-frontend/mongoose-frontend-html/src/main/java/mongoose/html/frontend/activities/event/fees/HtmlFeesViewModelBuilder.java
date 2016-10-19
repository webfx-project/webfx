package mongoose.html.frontend.activities.event.fees;

import elemental2.Element;
import mongoose.activities.frontend.event.fees.FeesViewModelBuilder;
import naga.framework.ui.i18n.I18n;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.util.HtmlUtil.getElementById;

/**
 * @author Bruno Salmon
 */
public class HtmlFeesViewModelBuilder extends FeesViewModelBuilder {

    @Override
    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        HtmlView htmlView = toolkit.createHtmlView(R.feesHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        new HtmlVPage(getElementById(rootElement, "content"))
                .setCenter(feesGroupsCollator);
        contentNode = htmlView;
        termsButton = toolkit.wrapNativeNode(getElementById(rootElement, "termsButton"));
        programButton = toolkit.wrapNativeNode(getElementById(rootElement, "programButton"));
    }

}
