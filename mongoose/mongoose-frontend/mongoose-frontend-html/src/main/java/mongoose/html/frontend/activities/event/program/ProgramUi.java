package mongoose.html.frontend.activities.event.program;

import elemental2.Element;
import elemental2.Node;
import mongoose.activities.frontend.event.program.ProgramViewModel;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.HtmlUtil.getElementById;
import static naga.providers.toolkit.html.HtmlUtil.setChild;

/**
 * @author Bruno Salmon
 */
public class ProgramUi {

    public static ProgramViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.programHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        I18n i18n = UiApplicationContext.getUiApplicationContext().getI18n();
        setChild(getElementById(rootElement, "content"), (Node)
                toolkit.createVBox(
                        HighLevelComponents.createSectionPanel("{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable", i18n),
                        HighLevelComponents.createSectionPanel("{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", i18n))
                        .unwrapToNativeNode());
        return new ProgramViewModel(htmlView,
                toolkit.wrapNativeNode(getElementById(rootElement, "previousButton")));
    }

}
