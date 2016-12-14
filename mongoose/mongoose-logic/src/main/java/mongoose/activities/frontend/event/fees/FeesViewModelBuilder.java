package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.highlevelcomponents.SectionPanelStyleOptions;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.fx.ext.cell.collator.GridCollator;
import naga.toolkit.fx.ext.cell.collator.NodeCollatorRegistry;
import naga.toolkit.fx.geometry.Insets;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.HBox;
import naga.toolkit.fx.scene.layout.VBox;

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
    protected void buildComponents(I18n i18n) {
        super.buildComponents(i18n);
        termsButton = Button.create();
        programButton = Button.create();
        feesGroupsCollator = new GridCollator(this::toFeesGroupPanel, NodeCollatorRegistry.vBoxCollator());
        buttonsBox = HBox.create(previousButton, termsButton, programButton, nextButton);
        contentNode = BorderPane.create(feesGroupsCollator, null, null , buttonsBox, null);
    }

    private Node toFeesGroupPanel(Node... nodes) {
/*
        BorderPane borderPane = buildFeesSectionPanel(nodes[0]);
        borderPane.setCenter(nodes[1]);
        borderPane.setBottom(nodes[2]);
        borderPane.setInsets(Insets.create(10, 30, 10, 30));
        return borderPane;
*/
        VBox vBox = VBox.create(nodes);
        vBox.setInsets(Insets.create(10, 30, 10, 30));
        return vBox;
    }

    private BorderPane buildFeesSectionPanel(Node node) {
        SectionPanelStyleOptions options = new SectionPanelStyleOptions(false);
        return HighLevelComponents.createSectionPanel(options, node);
    }
}
