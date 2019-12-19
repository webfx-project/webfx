package webfx.framework.client.ui.controls.button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import webfx.framework.client.services.i18n.I18nControls;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.action.ActionBinder;
import webfx.framework.client.ui.util.background.BackgroundBuilder;
import webfx.framework.client.ui.util.border.BorderBuilder;
import webfx.framework.client.ui.util.image.JsonImageViews;
import webfx.framework.client.ui.util.paint.PaintBuilder;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.kit.util.properties.Properties;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class ButtonBuilder {

    private Object iconUrlOrJson;
    private Node icon;

    private Object i18nKey;

    private Action action;
    private EventHandler<ActionEvent> onAction;

    private PaintBuilder textFillBuilder;
    private Paint textFill;

    private PaintBuilder pressedTextFillBuilder;
    private Paint pressedTextFill;

    private double height;

    private BorderBuilder borderBuilder;
    private Border border;

    private BackgroundBuilder backgroundBuilder;
    private Background background;
    private BackgroundBuilder pressedBackgroundBuilder;
    private Background pressedBackground;

    private boolean dropDownArrowDecorated;

    private Function<Button, Button> styleFunction;

    private Button button;

    public ButtonBuilder setIconUrlOrJson(Object iconUrlOrJson) {
        this.iconUrlOrJson = iconUrlOrJson;
        return this;
    }

    public ButtonBuilder setIcon(Node icon) {
        this.icon = icon;
        return this;
    }

    public ButtonBuilder setI18nKey(Object i18nKey) {
        this.i18nKey = i18nKey;
        return this;
    }

    public ButtonBuilder setAction(Action action) {
        this.action = action;
        return this;
    }

    public ButtonBuilder setOnAction(EventHandler<ActionEvent> onAction) {
        this.onAction = onAction;
        return this;
    }

    public ButtonBuilder setTextFillBuilder(PaintBuilder textFillBuilder) {
        this.textFillBuilder = textFillBuilder;
        return this;
    }

    public ButtonBuilder setTextFill(Paint textFill) {
        this.textFill = textFill;
        return this;
    }

    public ButtonBuilder setPressedTextFillBuilder(PaintBuilder pressedTextFillBuilder) {
        this.pressedTextFillBuilder = pressedTextFillBuilder;
        return this;
    }

    public ButtonBuilder setPressedTextFill(Paint pressedTextFill) {
        this.pressedTextFill = pressedTextFill;
        return this;
    }

    public ButtonBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    public ButtonBuilder setBorderBuilder(BorderBuilder borderBuilder) {
        this.borderBuilder = borderBuilder;
        return this;
    }

    public ButtonBuilder setBorder(Border border) {
        this.border = border;
        return this;
    }

    public ButtonBuilder setBackgroundBuilder(BackgroundBuilder backgroundBuilder) {
        this.backgroundBuilder = backgroundBuilder;
        return this;
    }

    public ButtonBuilder setBackground(Background background) {
        this.background = background;
        return this;
    }

    public ButtonBuilder setPressedBackgroundBuilder(BackgroundBuilder pressedBackgroundBuilder) {
        this.pressedBackgroundBuilder = pressedBackgroundBuilder;
        return this;
    }

    public ButtonBuilder setPressedBackground(Background pressedBackground) {
        this.pressedBackground = pressedBackground;
        return this;
    }

    public ButtonBuilder setButton(Button button) {
        this.button = button;
        return this;
    }

    public ButtonBuilder setDropDownArrowDecorated(boolean dropDownArrowDecorated) {
        this.dropDownArrowDecorated = dropDownArrowDecorated;
        return this;
    }

    public ButtonBuilder setStyleFunction(Function<Button, Button> styleFunction) {
        this.styleFunction = styleFunction;
        return this;
    }

    public Button build() {
        if (button == null) {
            button = new Button();
            if (action != null)
                ActionBinder.bindButtonToAction(button, action);
            else {
                if (i18nKey != null)
                    I18nControls.bindI18nProperties(button, i18nKey);
                if (icon == null && iconUrlOrJson != null)
                    icon = JsonImageViews.createImageView(iconUrlOrJson);
                if (icon != null)
                    button.setGraphic(icon);
            }
            if (onAction == null && action != null)
                onAction = action;
            if (onAction != null)
                button.setOnAction(onAction);
            if (height > 0) {
                button.setPrefHeight(height);
                LayoutUtil.setMinMaxHeightToPref(button);
            }
            if (border == null && borderBuilder != null)
                border = borderBuilder.build();
            if (border != null)
                button.setBorder(border);
            if (background == null && backgroundBuilder != null)
                background = backgroundBuilder.build();
            if (background != null) {
                if (pressedBackground == null && pressedBackgroundBuilder != null)
                    pressedBackground = pressedBackgroundBuilder.build();
                if (pressedBackground == null || pressedBackground == background)
                    button.setBackground(background);
                else
                    button.backgroundProperty().bind(Properties.compute(button.pressedProperty(), pressed -> pressed ? pressedBackground : background));
            }
            if (dropDownArrowDecorated)
                ButtonUtil.decorateButtonWithDropDownArrow(button);
            if (styleFunction != null)
                button = styleFunction.apply(button);
            if (textFill == null && textFillBuilder != null)
                textFill = textFillBuilder.build();
            if (textFill != null) {
                if (pressedTextFill != null && pressedTextFillBuilder != null)
                    pressedTextFill = pressedTextFillBuilder.build();
                if (pressedTextFill == null || pressedTextFill == textFill) {
                    button.textFillProperty().unbind();
                    button.setTextFill(textFill);
                } else
                    button.textFillProperty().bind(Properties.compute(button.pressedProperty(), pressed -> pressed ? pressedTextFill : textFill));
            }
        }
        return button;
    }
}
