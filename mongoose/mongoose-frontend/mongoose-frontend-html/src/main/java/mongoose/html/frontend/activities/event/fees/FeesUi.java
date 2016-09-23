package mongoose.html.frontend.activities.event.fees;

import elemental2.Element;
import mongoose.activities.frontend.event.fees.FeesViewModel;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.HtmlUtil.getElementById;

/**
 * @author Bruno Salmon
 */
public class FeesUi {

    public static FeesViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.feesHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        return new FeesViewModel(htmlView,
                toolkit.wrapNativeNode(getElementById(rootElement, "previousButton")),
                toolkit.wrapNativeNode(getElementById(rootElement, "nextButton")),
                toolkit.wrapNativeNode(getElementById(rootElement, "termsButton")),
                toolkit.wrapNativeNode(getElementById(rootElement, "programButton")));
    }

}
