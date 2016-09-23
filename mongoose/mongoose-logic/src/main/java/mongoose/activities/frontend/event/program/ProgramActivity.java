package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class ProgramActivity extends BookingProcessActivity<ProgramViewModel, ProgramPresentationModel> {

    public ProgramActivity() {
        super(ProgramPresentationModel::new, null);
    }

    protected ProgramViewModel buildView(Toolkit toolkit) {
        Button previousButton = toolkit.createButton();
        return new ProgramViewModel(toolkit.createVPage()
                .setCenter(toolkit.createVBox(
                        HighLevelComponents.createSectionPanel("{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable", getI18n()),
                        HighLevelComponents.createSectionPanel("{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", getI18n())))
                .setFooter(previousButton),
                previousButton);
    }

}
