package webfx.framework.client.ui.controls.button;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.controls.Controls;
import webfx.framework.client.ui.util.background.BackgroundUtil;
import webfx.framework.client.ui.util.border.BorderUtil;
import webfx.kit.util.properties.Properties;
import webfx.extras.imagestore.ImageStore;
import webfx.framework.client.ui.validation.controlsfx.control.decoration.GraphicDecoration;

/**
 * @author Bruno Salmon
 */
public final class ButtonUtil {

    public static Button newButton(Action action) {
        return newButtonBuilder(action).build();
    }

    public static ButtonBuilder newButtonBuilder(Action action) {
        return new ButtonBuilder().setAction(action);
    }

    public static Button newButton(Object iconUrlOrJson, Object translationKey, EventHandler<ActionEvent> onAction) {
        return newButtonBuilder(iconUrlOrJson, translationKey, onAction).build();
    }

    public static ButtonBuilder newButtonBuilder(Object iconUrlOrJson, Object translationKey, EventHandler<ActionEvent> onAction) {
        return new ButtonBuilder().setIconUrlOrJson(iconUrlOrJson).setI18nKey(translationKey).setOnAction(onAction);
    }

    public static Button newButton(Node graphic, Object translationKey, EventHandler<ActionEvent> onAction) {
        return newButtonBuilder(graphic, translationKey, onAction).build();
    }

    public static ButtonBuilder newButtonBuilder(Node graphic, Object translationKey, EventHandler<ActionEvent> onAction) {
        return new ButtonBuilder().setIcon(graphic).setI18nKey(translationKey).setOnAction(onAction);
    }

    public static Button newDropDownButton() {
        Button button = new Button();
        int radius = 6;
        button.setBorder(BorderUtil.newBorder(Color.LIGHTGRAY, radius, 1));
        button.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("white", "#E0E0E0", radius));
        return decorateButtonWithDropDownArrow(button);
    }

    public static Button decorateButtonWithDropDownArrow(Button button) {
        GraphicDecoration dropDownArrowDecoration = new GraphicDecoration(ImageStore.createImageView("images/s16/controls/dropDownArrow.png"), Pos.CENTER_RIGHT, 0, 0, -1, 0);
        Properties.runNowAndOnPropertiesChange(() -> Platform.runLater(() ->
            Controls.onSkinReady(button, () -> dropDownArrowDecoration.applyDecoration(button))
        ), button.graphicProperty());
        button.setMinWidth(0d);
        button.setMaxWidth(Double.MAX_VALUE);
        // Adding padding for the extra right icon decoration (adding the icon width 16px + repeating the 6px standard padding)
        button.setPadding(new Insets(3, 6 + 16 + 6, 3, 6));
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

    public static void resetDefaultAndCancelButtons(Button defaultButton, Button cancelButton) {
        resetDefaultButton(defaultButton);
        resetCancelButton(cancelButton);
    }
}
