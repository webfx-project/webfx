package mongoose.html.frontend.activities.event.terms;

import elemental2.Element;
import elemental2.Node;
import mongoose.activities.frontend.event.terms.TermsViewModel;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.activity.client.UiApplicationContext;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
import naga.toolkit.cell.collators.GridCollator;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.HtmlUtil.createNodeFromHtml;
import static naga.providers.toolkit.html.HtmlUtil.getElementById;
import static naga.providers.toolkit.html.HtmlUtil.setChild;

/**
 * @author Bruno Salmon
 */
public class TermsUi {

    public static TermsViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.termsHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        GridCollator termsLetterCollator = new GridCollator("vbox", "hbox");
        setChild(getElementById(rootElement, "content"), (Node)
                HighLevelComponents.createSectionPanel("{url: 'images/certificate.svg', width: 16, height: 16}", "TermsAndConditions", UiApplicationContext.getUiApplicationContext().getI18n())
                .setCenter(new HtmlVPage(createNodeFromHtml("<div class='form-control terms fullHeight'>")).setCenter(termsLetterCollator))
                .unwrapToNativeNode());
        return new TermsViewModel(htmlView,
                termsLetterCollator,
                toolkit.wrapNativeNode(getElementById(rootElement, "previousButton")));
    }

}
