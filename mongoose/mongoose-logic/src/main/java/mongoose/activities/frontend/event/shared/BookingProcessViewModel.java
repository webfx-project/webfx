package mongoose.activities.frontend.event.shared;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public class BookingProcessViewModel<N extends Node> extends AbstractViewModel<N> {

    private final Button previousButton;
    private final Button nextButton;

    public BookingProcessViewModel(N contentNode, Button previousButton, Button nextButton) {
        super(contentNode);
        this.previousButton = previousButton;
        this.nextButton = nextButton;
    }

    public Button getPreviousButton() {
        return previousButton;
    }

    public Button getNextButton() {
        return nextButton;
    }
}