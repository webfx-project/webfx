package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class FeesActivity extends BookingProcessActivity<FeesViewModel, FeesPresentationModel> {

    public FeesActivity() {
        super(FeesPresentationModel::new, "options");
    }

    protected FeesViewModel buildView(Toolkit toolkit) {
        Button previousButton = toolkit.createButton();
        Button nextButton = toolkit.createButton();
        return new FeesViewModel(toolkit.createVPage().setFooter(toolkit.createHBox(previousButton, nextButton)), previousButton, nextButton);
    }

}
