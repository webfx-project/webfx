package mongoose.activities.shared.highlevelcomponents;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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
        panel.setTop(NodeCollatorRegistry.hBoxCollator().collateNodes(headerNodes));
        return panel;
    }

    Node createBadge(Node... badgeNodes);

    Button createBookButton();

    Button createSoldoutButton();
}
