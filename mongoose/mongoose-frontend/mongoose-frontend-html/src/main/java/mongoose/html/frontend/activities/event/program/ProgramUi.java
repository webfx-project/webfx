package mongoose.html.frontend.activities.event.program;

import elemental2.Element;
import mongoose.activities.frontend.event.program.ProgramViewModel;
import naga.providers.toolkit.html.HtmlUtil;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;


/**
 * @author Bruno Salmon
 */
public class ProgramUi {

    public static ProgramViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.programHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        return new ProgramViewModel(htmlView,
                toolkit.wrapNativeNode(HtmlUtil.getElementById(rootElement, "previousButton")));
    }

}
