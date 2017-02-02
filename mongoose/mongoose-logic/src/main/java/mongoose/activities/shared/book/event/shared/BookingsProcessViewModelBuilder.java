package mongoose.activities.shared.book.event.shared;

import naga.framework.activity.combinations.viewapplication.ViewApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.ViewModelBuilder;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @author Bruno Salmon
 */
public abstract class BookingsProcessViewModelBuilder<VM extends BookingProcessViewModel> implements ViewModelBuilder<VM> {

    protected Button previousButton;
    protected Button nextButton;
    protected Node contentNode;

    @Override
    public VM buildViewModel() {
        buildComponents(ViewApplicationContext.getViewApplicationContext().getI18n());
        return createViewModel();
    }

    protected abstract VM createViewModel();

    protected void buildComponents(I18n i18n) {
        if (previousButton == null)
            previousButton = new Button();
        if (nextButton == null)
            nextButton = new Button();
        if (contentNode == null)
            contentNode = new BorderPane();
        assembleComponentsIntoContentNode();
    }

    protected void assembleComponentsIntoContentNode() {
        if (contentNode instanceof BorderPane)
            ((BorderPane) contentNode).setBottom(new HBox(previousButton, nextButton));
    }
}
