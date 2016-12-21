package mongoose.activities.frontend.event.terms;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import naga.toolkit.fxdata.DisplayResultSetControl;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public class TermsViewModel extends BookingProcessViewModel<Node> {

    private final DisplayResultSetControl termsLetterDisplayResultSetControl;

    public TermsViewModel(Node contentNode, DisplayResultSetControl termsLetterDisplayResultSetControl, Button previousButton) {
        super(contentNode, previousButton, null);
        this.termsLetterDisplayResultSetControl = termsLetterDisplayResultSetControl;
    }

    DisplayResultSetControl getTermsLetterDisplayResultSetControl() {
        return termsLetterDisplayResultSetControl;
    }
}
