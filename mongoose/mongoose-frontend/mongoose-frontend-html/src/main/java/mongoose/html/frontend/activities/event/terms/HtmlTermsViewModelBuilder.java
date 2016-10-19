package mongoose.html.frontend.activities.event.terms;

import elemental2.Element;
import elemental2.Node;
import mongoose.activities.frontend.event.terms.TermsViewModelBuilder;
import naga.framework.ui.i18n.I18n;
import naga.platform.spi.Platform;
import naga.providers.toolkit.html.nodes.controls.HtmlButton;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.util.HtmlUtil.*;

/**
 * @author Bruno Salmon
 */
public class HtmlTermsViewModelBuilder extends TermsViewModelBuilder {

    @Override
    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        HtmlView htmlView = toolkit.createHtmlView(R.termsHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        previousButton = new HtmlButton(getElementById(rootElement, "previousButton"));
        termsPanel.setCenter(new HtmlVPage(createNodeFromHtml("<div class='form-control terms fullHeight'></div>")).setCenter(termsLetterCollator));
        setChild(getElementById(rootElement, "content"), (Node) termsPanel.unwrapToNativeNode());
        contentNode = htmlView;
        Platform.log("8");
    }
}
