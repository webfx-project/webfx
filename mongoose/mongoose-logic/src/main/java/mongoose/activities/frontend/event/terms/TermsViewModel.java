package mongoose.activities.frontend.event.terms;

import mongoose.activities.frontend.event.booking.BookingProcessViewModel;
import naga.toolkit.spi.nodes.DisplayResultSetNode;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class TermsViewModel extends BookingProcessViewModel {

    private final DisplayResultSetNode termsLetterDisplayResultSetNode;

    public TermsViewModel(GuiNode contentNode, DisplayResultSetNode termsLetterDisplayResultSetNode, Button previousButton) {
        super(contentNode, previousButton, null);
        this.termsLetterDisplayResultSetNode = termsLetterDisplayResultSetNode;
    }

    DisplayResultSetNode getTermsLetterDisplayResultSetNode() {
        return termsLetterDisplayResultSetNode;
    }
}
