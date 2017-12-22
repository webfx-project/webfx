package mongoose.activities.shared.logic.ui.highlevelcomponents;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import naga.framework.ui.controls.BackgroundUtil;
import naga.framework.ui.controls.BorderUtil;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.cell.collator.NodeCollatorRegistry;

import static naga.framework.ui.controls.ImageViewUtil.createImageView;
import static naga.framework.ui.controls.TextUtil.createLabel;

/**
 * @author Bruno Salmon
 */
public interface HighLevelComponentsFactory {

    BorderPane createSectionPanel(SectionPanelStyleOptions options);

    default BorderPane createSectionPanel(SectionPanelStyleOptions options, String iconImageUrl, String translationKey, I18n i18n) {
        return createSectionPanel(options, createImageView(iconImageUrl), createLabel(translationKey, i18n));
    }

    default BorderPane createSectionPanel(SectionPanelStyleOptions options, Node... headerNodes) {
        BorderPane panel = createSectionPanel(options);
        panel.getStyleClass().add("section-panel");
        panel.setBorder(BorderUtil.newBorder(Color.grayRgb(0x0d), 5, 1));
        panel.setBackground(BackgroundUtil.newBackground(Color.WHITE));
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
