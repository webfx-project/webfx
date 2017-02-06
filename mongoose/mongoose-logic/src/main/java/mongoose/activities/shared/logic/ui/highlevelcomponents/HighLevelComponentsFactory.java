package mongoose.activities.shared.logic.ui.highlevelcomponents;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.cell.collator.NodeCollatorRegistry;

import static naga.framework.activity.view.impl.ViewActivityBase.createImageView;
import static naga.framework.activity.view.impl.ViewActivityBase.createTextView;

/**
 * @author Bruno Salmon
 */
public interface HighLevelComponentsFactory {

    BorderPane createSectionPanel(SectionPanelStyleOptions options);

    default BorderPane createSectionPanel(SectionPanelStyleOptions options, String iconImageUrl, String translationKey, I18n i18n) {
        return createSectionPanel(options, createImageView(iconImageUrl), createTextView(translationKey, i18n));
    }

    default BorderPane createSectionPanel(SectionPanelStyleOptions options, Node... headerNodes) {
        BorderPane panel = createSectionPanel(options);
        panel.setBorder(new Border(new BorderStroke(Color.grayRgb(0x0d), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        HBox hBox = (HBox) NodeCollatorRegistry.hBoxCollator().collateNodes(headerNodes);
        hBox.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf("from 0% 0% to 0% 100%, 0xF0F0F0 0%, 0xE0E0E0 100%"), new CornerRadii(5), null)));
        hBox.setMinHeight(50d);
        hBox.setPadding(new Insets(0, 10, 0 , 10));
        panel.setTop(hBox);
        return panel;
    }

    Node createBadge(Node... badgeNodes);

    Button createBookButton();

    Button createSoldoutButton();
}
