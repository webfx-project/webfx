package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class ProgramViewModelBuilder extends BookingsProcessViewModelBuilder<ProgramViewModel> {

    protected VPage calendarPanel;
    protected VPage teachingsPanel;
    protected VBox panelsVBox;

    @Override
    protected ProgramViewModel createViewModel() {
        return new ProgramViewModel(contentNode, calendarPanel, teachingsPanel, previousButton);
    }

    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable", i18n);
        teachingsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", i18n);
        panelsVBox = toolkit.createVBox(calendarPanel, teachingsPanel);
        super.buildComponents(toolkit, i18n);
    }

    @Override
    protected void assembleComponentsIntoContentNode(Toolkit toolkit) {
        if (contentNode instanceof VPage)
            ((VPage) contentNode)
                .setCenter(panelsVBox)
                .setFooter(previousButton);
    }
}
