package mongooses.core.activities.sharedends.logic.ui.highlevelcomponents;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.util.background.BackgroundUtil;
import webfx.framework.ui.util.border.BorderUtil;
import webfx.fxkits.extra.cell.collator.NodeCollatorRegistry;

import static webfx.framework.ui.util.image.JsonImageViews.createImageView;

/**
 * @author Bruno Salmon
 */
public interface HighLevelComponentsFactory {

    BorderPane createSectionPanel(SectionPanelStyleOptions options);

    default BorderPane createSectionPanel(SectionPanelStyleOptions options, String iconImageUrl, String translationKey) {
        return createSectionPanel(options, createImageView(iconImageUrl), I18n.translateText(new Label(), translationKey));
    }

    default BorderPane createSectionPanel(SectionPanelStyleOptions options, Node... headerNodes) {
        BorderPane panel = createSectionPanel(options);
        panel.getStyleClass().add("section-panel");
        panel.setBorder(BorderUtil.newBorder(Color.grayRgb(0x0d), 5, 1));
        panel.setBackground(BackgroundUtil.WHITE_BACKGROUND);
        HBox hBox = (HBox) NodeCollatorRegistry.hBoxCollator().collateNodes(headerNodes);
        hBox.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("0xF0F0F0", "0xE0E0E0",5));
        hBox.setMinHeight(40d);
        hBox.setPadding(new Insets(0, 10, 0 , 10));
        panel.setTop(hBox);
        return panel;
    }

    Node createBadge(Node... badgeNodes);

    Button createBookButton();

    Button createSoldoutButton();
}
