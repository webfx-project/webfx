package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class OptionsActivity extends BookingProcessActivity<OptionsViewModel, OptionsPresentationModel> {

    public OptionsActivity() {
        super(OptionsPresentationModel::new, "person");
    }

    protected OptionsViewModel buildView(Toolkit toolkit) {
        Button previousButton = toolkit.createButton();
        Button nextButton = toolkit.createButton();
        return new OptionsViewModel(toolkit.createVPage().setFooter(toolkit.createHBox(previousButton, nextButton)), previousButton, nextButton);
    }

}
