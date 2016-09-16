package mongoose.html.frontend.activities.event.terms;

import elemental2.HTMLDivElement;
import mongoose.activities.frontend.event.terms.TermsViewModel;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.cell.collators.GridCollator;
import naga.toolkit.spi.Toolkit;

import static mongoose.html.frontend.activities.application.MongooseFrontendGwtBundle.R;


/**
 * @author Bruno Salmon
 */
public class TermsUi {

    public static TermsViewModel buildView(Toolkit toolkit) {
        HTMLDivElement div = HtmlUtil.createDivElement(R.termsHtml().getText());
        GridCollator termsLetterCollator = new GridCollator("vbox", "hbox");
        new HtmlParent<>(HtmlUtil.getElementById(div, "termsLetterContainer")).getChildren().setAll(termsLetterCollator);
        return new TermsViewModel(new HtmlNode<>(div),
                termsLetterCollator,
                toolkit.wrapNativeNode(HtmlUtil.getElementById(div, "previousButton")));
    }

}
