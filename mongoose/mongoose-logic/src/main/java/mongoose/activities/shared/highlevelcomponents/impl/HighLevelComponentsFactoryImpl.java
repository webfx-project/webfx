package mongoose.activities.shared.highlevelcomponents.impl;

import mongoose.activities.shared.highlevelcomponents.HighLevelComponentsFactory;
import mongoose.activities.shared.highlevelcomponents.SectionPanelStyleOptions;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.HBox;

/**
 * @author Bruno Salmon
 */
public class HighLevelComponentsFactoryImpl implements HighLevelComponentsFactory {

    @Override
    public BorderPane createSectionPanel(SectionPanelStyleOptions options) {
        return BorderPane.create();
    }

    @Override
    public Node createBadge(Node... badgeNodes) {
        return HBox.create(badgeNodes);
    }

    @Override
    public Button createBookButton() {
        return Button.create();
    }

    @Override
    public Button createSoldoutButton() {
        return Button.create();
    }
}
