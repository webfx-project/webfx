package mongoose.html.frontend.activities.event.options;

import elemental2.Element;
import elemental2.Node;
import mongoose.activities.frontend.event.options.OptionsViewModelBuilder;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.util.HtmlUtil.getElementById;
import static naga.providers.toolkit.html.util.HtmlUtil.setChild;

/**
 * @author Bruno Salmon
 */
public class HtmlOptionsViewModelBuilder extends OptionsViewModelBuilder {

    @Override
    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        HtmlView htmlView = toolkit.createHtmlView(R.optionsHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        previousButton = toolkit.wrapNativeNode(getElementById(rootElement, "previousButton"));
        nextButton = toolkit.wrapNativeNode(getElementById(rootElement, "nextButton"));
        contentNode = htmlView;
        super.buildComponents(toolkit, i18n);
    }

    @Override
    protected void assembleComponentsIntoContentNode(Toolkit toolkit) {
        HtmlView htmlView = (HtmlView) contentNode;
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        setChild(getElementById(rootElement, "content"), (Node) calendarPanel.unwrapToNativeNode());
    }
}
