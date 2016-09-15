package mongoose.html.frontend.activities.event.options;

import elemental2.HTMLDivElement;
import mongoose.activities.frontend.event.options.OptionsViewModel;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.Toolkit;

import static mongoose.html.frontend.activities.application.MongooseFrontendGwtBundle.R;


/**
 * @author Bruno Salmon
 */
public class OptionsUi {

    public static OptionsViewModel buildView(Toolkit toolkit) {
        HTMLDivElement div = HtmlUtil.createDivElement(R.optionsHtml().getText());
        return new OptionsViewModel(new HtmlNode<>(div),
                toolkit.wrapNativeNode(HtmlUtil.getElementById(div, "previousButton")),
                toolkit.wrapNativeNode(HtmlUtil.getElementById(div, "nextButton")));
    }

}
