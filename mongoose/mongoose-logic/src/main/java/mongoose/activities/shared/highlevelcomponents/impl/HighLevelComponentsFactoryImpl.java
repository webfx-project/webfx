package mongoose.activities.shared.highlevelcomponents.impl;

import mongoose.activities.shared.highlevelcomponents.HighLevelComponentsFactory;
import mongoose.activities.shared.highlevelcomponents.SectionPanelStyleOptions;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;
import naga.fx.scene.layout.BorderPane;
import naga.fx.scene.layout.HBox;

/**
 * @author Bruno Salmon
 */
public class HighLevelComponentsFactoryImpl implements HighLevelComponentsFactory {

    @Override
    public BorderPane createSectionPanel(SectionPanelStyleOptions options) {
        return new BorderPane();
    }

    @Override
    public Node createBadge(Node... badgeNodes) {
        return new HBox(badgeNodes);
    }

    @Override
    public Button createBookButton() {
        return new Button();
    }

    @Override
    public Button createSoldoutButton() {
        return new Button();
    }
}
