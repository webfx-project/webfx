package mongooses.core.activities.sharedends.logic.ui.highlevelcomponents.impl;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import mongooses.core.activities.sharedends.logic.ui.highlevelcomponents.HighLevelComponentsFactory;
import mongooses.core.activities.sharedends.logic.ui.highlevelcomponents.SectionPanelStyleOptions;
import webfx.framework.ui.graphic.background.BackgroundUtil;
import webfx.framework.ui.graphic.border.BorderUtil;

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
        return createToBottomGradientButton("#7fd504", "#2a8236");
    }

    @Override
    public Button createSoldoutButton() {
        return createToBottomGradientButton("#e92c04", "#853416");
    }

    private static Button createToBottomGradientButton(String topWebColor, String bottomWebColor) {
        return createButton(topWebColor + " 0%, " + bottomWebColor + " 100%", bottomWebColor);
    }

    private static Button createButton(String linearGradient, String borderWebColor) {
        Button button = new Button();
        button.setBackground(BackgroundUtil.newLinearGradientBackground(linearGradient, 5));
        button.setBorder(BorderUtil.newBorder(Color.web(borderWebColor), 5));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-text-fill: white;");
        return button;
    }
}
