package mongoose.html.frontend.activities.event.options;

import elemental2.Element;
import mongoose.activities.frontend.event.options.OptionsViewModel;
import naga.providers.toolkit.html.HtmlUtil;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;


/**
 * @author Bruno Salmon
 */
public class OptionsUi {

    public static OptionsViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.optionsHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        return new OptionsViewModel(htmlView,
                toolkit.wrapNativeNode(HtmlUtil.getElementById(rootElement, "previousButton")),
                toolkit.wrapNativeNode(HtmlUtil.getElementById(rootElement, "nextButton")));
    }

}
