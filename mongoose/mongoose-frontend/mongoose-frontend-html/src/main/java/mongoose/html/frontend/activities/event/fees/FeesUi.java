package mongoose.html.frontend.activities.event.fees;

import elemental2.HTMLDivElement;
import mongoose.activities.frontend.event.fees.FeesViewModel;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.Toolkit;

import static mongoose.html.frontend.activities.application.MongooseFrontendGwtBundle.R;


/**
 * @author Bruno Salmon
 */
public class FeesUi {

    public static FeesViewModel buildView(Toolkit toolkit) {
        HTMLDivElement div = HtmlUtil.createDivElement(R.feesHtml().getText());
        return new FeesViewModel(new HtmlNode<>(div),
                toolkit.wrapNativeNode(HtmlUtil.getElementById(div, "previousButton")),
                toolkit.wrapNativeNode(HtmlUtil.getElementById(div, "nextButton")),
                toolkit.wrapNativeNode(HtmlUtil.getElementById(div, "programButton")));
    }

}
