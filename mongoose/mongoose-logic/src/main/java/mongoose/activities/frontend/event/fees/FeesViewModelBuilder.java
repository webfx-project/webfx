package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.highlevelcomponents.SectionPanelStyleOptions;
import naga.framework.ui.i18n.I18n;
import javafx.geometry.Insets;
import naga.fxdata.cell.collator.GridCollator;
import naga.fxdata.cell.collator.NodeCollatorRegistry;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
        termsButton = new Button();
        programButton = new Button();
        feesGroupsCollator = new GridCollator(this::toFeesGroupPanel, NodeCollatorRegistry.vBoxCollator());
        buttonsBox = new HBox(previousButton, termsButton, programButton, nextButton);
        contentNode = new BorderPane(feesGroupsCollator, null, null, buttonsBox, null);
    }

    private Node toFeesGroupPanel(Node... nodes) {
        BorderPane borderPane = buildFeesSectionPanel(nodes[0]);
        borderPane.setCenter(nodes[1]);
        borderPane.setBottom(nodes[2]);
        borderPane.setPadding(new Insets(10, 30, 10, 30));
        return borderPane;
    }

    private BorderPane buildFeesSectionPanel(Node node) {
        SectionPanelStyleOptions options = new SectionPanelStyleOptions(false);
        return HighLevelComponents.createSectionPanel(options, node);
    }
}
