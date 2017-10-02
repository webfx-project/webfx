package mongoose.activities.shared.logic.ui.highlevelcomponents.impl;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponentsFactory;
import mongoose.activities.shared.logic.ui.highlevelcomponents.SectionPanelStyleOptions;
import naga.framework.ui.controls.BackgroundUtil;
import naga.framework.ui.controls.BorderUtil;

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
        return createButton("#7fd504", "#2a8236");
    }

    @Override
    public Button createSoldoutButton() {
        return createButton("#e92c04", "#853416");
    }

    private static Button createButton(String topWebColor, String bottomWebColor) {
        Button button = new Button();
        button.setBackground(BackgroundUtil.newVerticalLinearGradientBackground(topWebColor, bottomWebColor, 5));
        button.setBorder(BorderUtil.newBorder(Color.web(bottomWebColor), 5));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-text-fill: white;");
        return button;
    }
}
