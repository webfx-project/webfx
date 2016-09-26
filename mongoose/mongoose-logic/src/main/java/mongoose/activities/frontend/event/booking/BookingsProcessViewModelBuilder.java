package mongoose.activities.frontend.event.booking;

import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.ViewBuilder;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

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
        previousButton = toolkit.createButton();
        nextButton = toolkit.createButton();
        contentNode = toolkit.createVPage().setFooter(toolkit.createHBox(previousButton, nextButton));
    }
}
