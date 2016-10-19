package mongoose.html.frontend.activities.event.program;

import elemental2.Element;
import elemental2.Node;
import mongoose.activities.frontend.event.program.ProgramViewModelBuilder;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.util.HtmlUtil.getElementById;
import static naga.providers.toolkit.html.util.HtmlUtil.setChild;

/**
 * @author Bruno Salmon
 */
public class HtmlProgramViewModelBuilder extends ProgramViewModelBuilder {

    @Override
    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        HtmlView htmlView = toolkit.createHtmlView(R.programHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        setChild(getElementById(rootElement, "content"), (Node) panelsVBox.unwrapToNativeNode());
        previousButton = toolkit.wrapNativeNode(getElementById(rootElement, "previousButton"));
        contentNode = htmlView;
    }

}
