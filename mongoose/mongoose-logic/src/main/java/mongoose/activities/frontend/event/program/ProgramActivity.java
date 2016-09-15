package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;
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
        return new ProgramViewModel(toolkit.createVPage().setFooter(previousButton), previousButton);
    }

}
