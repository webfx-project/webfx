package mongoose.html.frontend.activities.event.terms;

import elemental2.Element;
import mongoose.activities.frontend.event.terms.TermsViewModel;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.cell.collators.GridCollator;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;


/**
 * @author Bruno Salmon
 */
public class TermsUi {

    public static TermsViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.termsHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        GridCollator termsLetterCollator = new GridCollator("vbox", "hbox");
        new HtmlParent<>(HtmlUtil.getElementById(rootElement, "termsLetterContainer")).getChildren().setAll(termsLetterCollator);
        return new TermsViewModel(htmlView,
                termsLetterCollator,
                toolkit.wrapNativeNode(HtmlUtil.getElementById(rootElement, "previousButton")));
    }

}
