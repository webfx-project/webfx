package naga.framework.ui.controls;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import naga.framework.ui.action.Action;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.fx.util.ImageStore;
import org.controlsfx.control.decoration.GraphicDecoration;

/**
 * @author Bruno Salmon
 */
public class ButtonUtil {

    public static Button newButton(Action action, I18n i18n) {
        return newButtonBuilder(action, i18n).build();
    }

    public static ButtonBuilder newButtonBuilder(Action action, I18n i18n) {
        return new ButtonBuilder().setAction(action).setI18n(i18n);
    }

    public static Button newButton(Object iconUrlOrJson, Object translationKey, I18n i18n, EventHandler<ActionEvent> onAction) {
        return newButtonBuilder(iconUrlOrJson, translationKey, i18n, onAction).build();
    }

    public static ButtonBuilder newButtonBuilder(Object iconUrlOrJson, Object translationKey, I18n i18n, EventHandler<ActionEvent> onAction) {
        return new ButtonBuilder().setIconUrlOrJson(iconUrlOrJson).setI18n(i18n).setI18nKey(translationKey).setOnAction(onAction);
    }

    public static Button newButton(Node graphic, Object translationKey, I18n i18n, EventHandler<ActionEvent> onAction) {
        return newButtonBuilder(graphic, translationKey, i18n, onAction).build();
    }

    public static ButtonBuilder newButtonBuilder(Node graphic, Object translationKey, I18n i18n, EventHandler<ActionEvent> onAction) {
        return new ButtonBuilder().setIcon(graphic).setI18n(i18n).setI18nKey(translationKey).setOnAction(onAction);
    }

    public static Button newDropDownButton() {
        return decorateButtonWithDropDownArrow(new Button());
    }

    public static Button decorateButtonWithDropDownArrow(Button button) {
        GraphicDecoration dropDownArrowDecoration = new GraphicDecoration(ImageStore.createImageView("images/16/dropDownArrow.png"), Pos.CENTER_RIGHT, 0, 0, -1, 0);
        Properties.runNowAndOnPropertiesChange(p -> Platform.runLater(() ->
            Controls.onSkinReady(button, () -> dropDownArrowDecoration.applyDecoration(button))
        ), button.graphicProperty());
        button.setMinWidth(0d);
        // Adding padding for the extra right icon decoration (adding the icon width 16px + repeating the 6px standard padding)
        button.setPadding(new Insets(6, 6 + 16 + 6, 6, 6));
        button.setAlignment(Pos.CENTER_LEFT);
        return button;
    }

    public static void resetDefaultButton(Button button) {
        // Resetting a default button which is required for JavaFx for the cases when the button is displayed a second time
        button.setDefaultButton(false);
        button.setDefaultButton(true);
    }

    public static void resetCancelButton(Button button) {
        // Resetting a cancel button which is required for JavaFx for the cases when the button is displayed a second time
        button.setCancelButton(false);
        button.setCancelButton(true);
    }
}
