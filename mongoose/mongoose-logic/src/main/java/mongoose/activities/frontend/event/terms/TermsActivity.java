package mongoose.activities.frontend.event.terms;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class TermsActivity extends BookingProcessActivity<TermsViewModel, TermsPresentationModel> {

    public TermsActivity() {
        super(TermsPresentationModel::new, null);
    }

    protected TermsViewModel buildView(Toolkit toolkit) {
        Button previousButton = toolkit.createButton();
        return new TermsViewModel(toolkit.createVPage().setFooter(previousButton), previousButton);
    }

}
