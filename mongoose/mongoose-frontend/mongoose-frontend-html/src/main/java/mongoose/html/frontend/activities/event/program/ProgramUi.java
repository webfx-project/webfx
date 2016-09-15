package mongoose.html.frontend.activities.event.program;

import elemental2.HTMLDivElement;
import mongoose.activities.frontend.event.program.ProgramViewModel;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.Toolkit;

import static mongoose.html.frontend.activities.application.MongooseFrontendGwtBundle.R;


/**
 * @author Bruno Salmon
 */
public class ProgramUi {

    public static ProgramViewModel buildView(Toolkit toolkit) {
        HTMLDivElement div = HtmlUtil.createDivElement(R.programHtml().getText());
        return new ProgramViewModel(new HtmlNode<>(div),
                toolkit.wrapNativeNode(HtmlUtil.getElementById(div, "previousButton")));
    }

}
