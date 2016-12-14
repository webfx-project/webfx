package mongoose.activities.frontend.event.shared;

import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.ViewBuilder;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.HBox;
import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public abstract class BookingsProcessViewModelBuilder<VM extends BookingProcessViewModel> implements ViewBuilder<VM> {

    protected Button previousButton;
    protected Button nextButton;
    protected Node contentNode;

    @Override
    public VM buildView(Toolkit toolkit) {
        buildComponents(toolkit, UiApplicationContext.getUiApplicationContext().getI18n());
        return createViewModel();
    }

    protected abstract VM createViewModel();

    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        if (previousButton == null)
            previousButton = Button.create();
        if (nextButton == null)
            nextButton = Button.create();
        if (contentNode == null)
            contentNode = BorderPane.create();
        assembleComponentsIntoContentNode(toolkit);
    }

    protected void assembleComponentsIntoContentNode(Toolkit toolkit) {
        if (contentNode instanceof BorderPane)
            ((BorderPane) contentNode).setBottom(HBox.create(previousButton, nextButton));
    }
}
