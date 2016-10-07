package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.cell.collators.GridCollator;
import naga.toolkit.cell.collators.NodeCollatorRegistry;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class FeesViewModelBuilder extends BookingsProcessViewModelBuilder<FeesViewModel> {

    protected Button termsButton;
    protected Button programButton;
    protected HBox buttonsBox;
    protected GridCollator feesGroupsCollator;

    @Override
    protected FeesViewModel createViewModel() {
        return new FeesViewModel(contentNode, feesGroupsCollator, previousButton, nextButton, termsButton, programButton);
    }

    @Override
    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        termsButton = toolkit.createButton();
        programButton = toolkit.createButton();
        feesGroupsCollator = new GridCollator(this::toFeesGroupPanel, NodeCollatorRegistry.vBoxCollator());
        buttonsBox = toolkit.createHBox(previousButton, termsButton, programButton, nextButton);
        contentNode = toolkit.createVPage()
                .setCenter(feesGroupsCollator)
                .setFooter(buttonsBox);
    }

    private GuiNode toFeesGroupPanel(GuiNode[] nodes) { // for GWT 2.8 beta1
        return buildFeesSectionPanel(nodes[0]).setCenter(nodes[1]);
    }

    private VPage buildFeesSectionPanel(GuiNode node) {
        return buildFeesSectionPanel(node, null);
    }

    private VPage buildFeesSectionPanel(GuiNode node, I18n i18n) {
        String imageJson = "{url: 'images/price-tag.svg', width: 16, height: 16}";
        if (node == null)
            return HighLevelComponents.createSectionPanel(imageJson, "Fees", i18n);
        return HighLevelComponents.createSectionPanel(PresentationActivity.createImage(imageJson), node);
    }
}
