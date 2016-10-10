package mongoose.html.frontend.activities.highlevelcomponents;

import elemental2.HTMLDivElement;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.highlevelcomponents.SectionPanelStyleOptions;
import mongoose.activities.shared.highlevelcomponents.impl.HighLevelComponentsFactoryImpl;
import naga.providers.toolkit.html.nodes.layouts.HtmlHBox;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;

import static naga.providers.toolkit.html.HtmlUtil.*;

/**
 * @author Bruno Salmon
 */
public class HtmlHighLevelComponentsFactory extends HighLevelComponentsFactoryImpl {

    public static void register() {
        HighLevelComponents.register(new HtmlHighLevelComponentsFactory());
    }

    @Override
    public VPage createSectionPanel(SectionPanelStyleOptions options) {
        String template = "" +
                "<div class='panel panel-default " + (options == null || options.hasPadding() ? "" : "noPadding") + "'>\n" +
                "    <div class='panel-heading'>\n" +
                "        <h4 id='section-header' class='panel-title'></h4>\n" +
                "    </div>\n" +
                "    <div class='panel-collapse collapse in'>\n" +
                "        <div id='section-center' class='panel-body'></div>\n" +
                "    </div>\n" +
                "    <div id='section-footer'></div>\n" +
                "</div>\n";
        HTMLDivElement div = createNodeFromHtml(template);
        return new HtmlVPage(div, getElementById(div, "section-header"), getElementById(div, "section-center"), getElementById(div, "section-footer"));
    }

    @Override
    public GuiNode createBadge(GuiNode... badgeNodes) {
        return Toolkit.setAllChildren(new HtmlHBox(setPseudoClass(createSpanElement(), "badge")), badgeNodes);
    }
}
