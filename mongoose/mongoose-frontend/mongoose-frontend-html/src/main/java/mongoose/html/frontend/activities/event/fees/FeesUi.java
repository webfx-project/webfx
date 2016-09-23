package mongoose.html.frontend.activities.event.fees;

import elemental2.Element;
import mongoose.activities.frontend.event.fees.FeesViewModel;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
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
        I18n i18n = UiApplicationContext.getUiApplicationContext().getI18n();
        new HtmlVPage(getElementById(rootElement, "content"))
                        .setCenter(HighLevelComponents.createSectionPanel("{url: 'images/price-tag.svg', width: 16, height: 16}", "Fees", i18n));
        return new FeesViewModel(htmlView,
                toolkit.createButton(),
                toolkit.createButton(),
                toolkit.wrapNativeNode(getElementById(rootElement, "termsButton")),
                toolkit.wrapNativeNode(getElementById(rootElement, "programButton")));
    }

}
