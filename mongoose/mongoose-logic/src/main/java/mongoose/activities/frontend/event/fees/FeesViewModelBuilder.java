package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class FeesViewModelBuilder extends BookingsProcessViewModelBuilder<FeesViewModel> {

    protected Button termsButton;
    protected Button programButton;
    protected VPage feesPanel;
    protected HBox buttonsBox;

    @Override
    protected FeesViewModel createViewModel() {
        return new FeesViewModel(contentNode, previousButton, nextButton, termsButton, programButton);
    }

    @Override
    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        termsButton = toolkit.createButton();
        programButton = toolkit.createButton();
        feesPanel = HighLevelComponents.createSectionPanel("{url: 'images/price-tag.svg', width: 16, height: 16}", "Fees", i18n);
        buttonsBox = toolkit.createHBox(previousButton, termsButton, programButton, nextButton);
        contentNode = toolkit.createVPage()
                .setCenter(feesPanel)
                .setFooter(buttonsBox);
    }
}
