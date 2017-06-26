package mongoose.activities.shared.generic;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.ui.theme.Theme;
import naga.framework.ui.action.ButtonFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface MongooseButtonFactoryMixin extends ButtonFactoryMixin {

    @Override
    default Button styleButton(Button button) {
        button.textFillProperty().bind(Theme.mainTextFillProperty());
        return button;
    }

    default BorderPane createSectionPanel(String i18nKey) {
        return HighLevelComponents.createSectionPanel(null, null, i18nKey, getI18n());
    }

    default Node createSectionPanel(String iconImageUrl, String translationKey, ObservableValue<Node> centerProperty) {
        BorderPane sectionPanel = createSectionPanel(iconImageUrl, translationKey);
        sectionPanel.centerProperty().bind(centerProperty);
        return sectionPanel;
    }

    default BorderPane createSectionPanel(String iconImageUrl, String translationKey) {
        return HighLevelComponents.createSectionPanel(null, iconImageUrl, translationKey, getI18n());
    }

}
