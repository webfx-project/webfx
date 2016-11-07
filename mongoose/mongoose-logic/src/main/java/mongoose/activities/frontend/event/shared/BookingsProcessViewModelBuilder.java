package mongoose.activities.frontend.event.shared;

import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.ViewBuilder;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public abstract class BookingsProcessViewModelBuilder<VM extends BookingProcessViewModel> implements ViewBuilder<VM> {

    protected Button previousButton;
    protected Button nextButton;
    protected GuiNode contentNode;

    @Override
    public VM buildView(Toolkit toolkit) {
        buildComponents(toolkit, UiApplicationContext.getUiApplicationContext().getI18n());
        return createViewModel();
    }

    protected abstract VM createViewModel();

    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        if (previousButton == null)
            previousButton = toolkit.createButton();
        if (nextButton == null)
            nextButton = toolkit.createButton();
        if (contentNode == null)
            contentNode = toolkit.createVPage();
        assembleComponentsIntoContentNode(toolkit);
    }

    protected void assembleComponentsIntoContentNode(Toolkit toolkit) {
        if (contentNode instanceof VPage)
            ((VPage) contentNode).setFooter(toolkit.createHBox(previousButton, nextButton));
    }
}
