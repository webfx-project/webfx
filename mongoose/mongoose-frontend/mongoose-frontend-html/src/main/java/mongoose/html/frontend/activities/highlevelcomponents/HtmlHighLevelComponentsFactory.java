package mongoose.html.frontend.activities.highlevelcomponents;

import elemental2.HTMLDivElement;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.highlevelcomponents.SectionPanelStyleOptions;
import mongoose.activities.shared.highlevelcomponents.impl.HighLevelComponentsFactoryImpl;
import naga.providers.toolkit.html.nodes.controls.HtmlButton;
import naga.providers.toolkit.html.nodes.layouts.HtmlHBox;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

import static naga.providers.toolkit.html.util.HtmlUtil.*;

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
                "    <div id='section-footer' class='panel-footer'></div>\n" +
                "</div>\n";
        HTMLDivElement div = createNodeFromHtml(template);
        return new HtmlVPage(div, getElementById(div, "section-header"), getElementById(div, "section-center"), getElementById(div, "section-footer"));
    }

    @Override
    public GuiNode createBadge(GuiNode... badgeNodes) {
        return Toolkit.setAllChildren(new HtmlHBox(setPseudoClass(createSpanElement(), "badge")), badgeNodes);
    }

    @Override
    public Button createBookButton() {
        return createButton(true);
    }

    @Override
    public Button createSoldoutButton() {
        return createButton(false);
    }

    public Button createButton(boolean success) {
        return new HtmlButton(setStyle(setPseudoClass(createButtonElement(), "btn btn-lg " + (success ? "btn-success" : "btn-danger")), "padding-top: 1px; padding-bottom: 1px; margin-bottom: 1px;"));
    }
}
