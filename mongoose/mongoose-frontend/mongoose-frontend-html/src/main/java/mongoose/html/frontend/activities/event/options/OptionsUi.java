package mongoose.html.frontend.activities.event.options;

import elemental2.Element;
import mongoose.activities.frontend.event.options.OptionsViewModel;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.util.HtmlUtil.getElementById;

/**
 * @author Bruno Salmon
 */
public class OptionsUi {

    public static OptionsViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.optionsHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        return new OptionsViewModel(htmlView,
                toolkit.wrapNativeNode(getElementById(rootElement, "previousButton")),
                toolkit.wrapNativeNode(getElementById(rootElement, "nextButton")));
    }

}
