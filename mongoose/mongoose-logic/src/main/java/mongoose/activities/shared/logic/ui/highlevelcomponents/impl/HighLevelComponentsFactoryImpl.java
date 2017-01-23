package mongoose.activities.shared.logic.ui.highlevelcomponents.impl;

import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponentsFactory;
import mongoose.activities.shared.logic.ui.highlevelcomponents.SectionPanelStyleOptions;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
