package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class OptionsViewModelBuilder extends BookingsProcessViewModelBuilder<OptionsViewModel> {

    protected VPage calendarPanel;

    @Override
    protected OptionsViewModel createViewModel() {
        return new OptionsViewModel(contentNode, calendarPanel, previousButton, nextButton);
    }

    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Attendance", i18n);
        super.buildComponents(toolkit, i18n);
    }

    @Override
    protected void assembleComponentsIntoContentNode(Toolkit toolkit) {
        super.assembleComponentsIntoContentNode(toolkit); // footer
        if (contentNode instanceof VPage)
            ((VPage) contentNode).setCenter(calendarPanel);
    }
}
