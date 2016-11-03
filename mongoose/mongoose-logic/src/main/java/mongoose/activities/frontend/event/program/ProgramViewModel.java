package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class ProgramViewModel extends BookingProcessViewModel {

    private final VPage calendarPanel;
    private final VPage teachingsPanel;

    public ProgramViewModel(GuiNode contentNode, VPage calendarPanel, VPage teachingsPanel, Button previousButton) {
        super(contentNode, previousButton, null);
        this.calendarPanel = calendarPanel;
        this.teachingsPanel = teachingsPanel;
    }

    public VPage getCalendarPanel() {
        return calendarPanel;
    }

    public VPage getTeachingsPanel() {
        return teachingsPanel;
    }
}
