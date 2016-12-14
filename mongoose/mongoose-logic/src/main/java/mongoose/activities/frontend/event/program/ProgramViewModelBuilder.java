package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.VBox;

/**
 * @author Bruno Salmon
 */
public class ProgramViewModelBuilder extends BookingsProcessViewModelBuilder<ProgramViewModel> {

    protected BorderPane calendarPanel;
    protected BorderPane teachingsPanel;
    protected VBox panelsVBox;

    @Override
    protected ProgramViewModel createViewModel() {
        return new ProgramViewModel(contentNode, calendarPanel, teachingsPanel, previousButton);
    }

    protected void buildComponents(I18n i18n) {
        calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable", i18n);
        teachingsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", i18n);
        panelsVBox = VBox.create(calendarPanel/*, teachingsPanel*/);
        super.buildComponents(i18n);
    }

    @Override
    protected void assembleComponentsIntoContentNode() {
        if (contentNode instanceof BorderPane) {
            BorderPane pane = (BorderPane) this.contentNode;
            pane.setCenter(panelsVBox);
            pane.setBottom(previousButton);
        }
    }
}
