package webfx.framework.client.ui.controls;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import webfx.framework.client.services.i18n.I18nControls;
import webfx.framework.client.ui.action.*;
import webfx.framework.client.ui.action.impl.SeparatorAction;
import webfx.framework.client.ui.controls.button.ButtonBuilder;
import webfx.kit.util.properties.ObservableLists;

import java.util.function.Supplier;

/**
 * @author Bruno Salmon
 */
public interface ControlFactoryMixin extends ActionFactoryMixin {

    default Button newButton() {
        return newButtonBuilder().build();
    }

    default ButtonBuilder newButtonBuilder() {
        return new ButtonBuilder().setStyleFunction(this::styleButton);
    }

    default Button newButton(Object i18nKey) {
        return newButtonBuilder(i18nKey).build();
    }

    default ButtonBuilder newButtonBuilder(Object i18nKey) {
        return newButtonBuilder().setI18nKey(i18nKey);
    }

    default Button newButton(Object i18nKey, EventHandler<ActionEvent> actionHandler) {
        return newButtonBuilder(i18nKey, actionHandler).build();
    }

    default ButtonBuilder newButtonBuilder(Object i18nKey, EventHandler<ActionEvent> actionHandler) {
        return newButtonBuilder(i18nKey).setOnAction(actionHandler);
    }

    default Button newButton(ActionBuilder actionBuilder) {
        return newButtonBuilder(actionBuilder.build()).build();
    }

    default Button newButton(Action action) {
        return newButtonBuilder(action).build();
    }

    default MenuItem newMenuItem(Action action) {
        if (action instanceof SeparatorAction)
            return new SeparatorMenuItem();
        MenuItem menuItem;
        if (!(action instanceof ActionGroup))
            menuItem = new MenuItem();
        else {
            Menu menu = new Menu();
            bindMenuItemsToActionGroup(menu.getItems(), (ActionGroup) action);
            menuItem = menu;
        }
        ActionBinder.bindMenuItemToAction(menuItem, action);
        return menuItem;
    }

    default ContextMenu newContextMenu(ActionGroup actionGroup) {
        ContextMenu contextMenu = new ContextMenu();
        bindMenuItemsToActionGroup(contextMenu.getItems(), actionGroup);
        return contextMenu;
    }

    default void bindMenuItemsToActionGroup(ObservableList<MenuItem> menuItems, ActionGroup actionGroup) {
        ObservableLists.bindConverted(menuItems, actionGroup.getVisibleActions(), this::newMenuItem);
    }

    default void setUpContextMenu(Node node, Supplier<ActionGroup> contextMenuActionGroupFactory) {
        node.setOnContextMenuRequested(e -> getOrCreateContextMenu(node, contextMenuActionGroupFactory).show(node, e.getScreenX(), e.getScreenY()));
    }

    default ContextMenu getOrCreateContextMenu(Node node, Supplier<ActionGroup> contextMenuActionGroupFactory) {
        ContextMenu contextMenu = getContextMenu(node);
        if (contextMenu == null)
            setContextMenu(node, contextMenu = newContextMenu(contextMenuActionGroupFactory.get()));
        return contextMenu;
    }

    default ContextMenu getContextMenu(Node node) {
        if (node instanceof Control)
            return ((Control) node).getContextMenu();
        return (ContextMenu) node.getProperties().get("contextMenu");
    }

    default void setContextMenu(Node node, ContextMenu contextMenu) {
        if (node instanceof Control)
            ((Control) node).setContextMenu(contextMenu);
        else
            node.getProperties().put("contextMenu", contextMenu);
    }

    default ButtonBuilder newButtonBuilder(Action action) {
        return newButtonBuilder().setAction(action);
    }

    default Button styleButton(Button button) {
        return button;
    }

    default CheckBox newCheckBox(Object i18nKey) {
        return I18nControls.bindI18nProperties(new CheckBox(), i18nKey);
    }

    default RadioButton newRadioButton(Object i18nKey) {
        return I18nControls.bindI18nProperties(new RadioButton(), i18nKey);
    }

    default RadioButton newRadioButton(Object i18nKey, ToggleGroup toggleGroup) {
        RadioButton radioButton = newRadioButton(i18nKey);
        radioButton.setToggleGroup(toggleGroup);
        return radioButton;
    }

    default Label newLabel(Object i18nKey) {
        return I18nControls.bindI18nProperties(new Label(), i18nKey);
    }

    default TextField newTextField() {
        return new TextField();
    }

    default TextField newTextField(Object i18nKey) {
        return I18nControls.bindI18nProperties(newTextField(), i18nKey);
    }

    default PasswordField newPasswordField() {
        return new PasswordField();
    }

    default Hyperlink newHyperlink() {
        return new Hyperlink();
    }

    default Hyperlink newHyperlink(Object i18nKey) {
        return I18nControls.bindI18nProperties(newHyperlink(), i18nKey);
    }

    default Hyperlink newHyperlink(Object i18nKey, EventHandler<ActionEvent> onAction) {
        Hyperlink hyperlink = I18nControls.bindI18nProperties(newHyperlink(), i18nKey);
        hyperlink.setOnAction(onAction);
        return hyperlink;
    }

    default TextArea newTextArea(Object i18nKey) {
        return I18nControls.bindI18nProperties(new TextArea(), i18nKey);
    }

    default Text newText(Object i18nKey) {
        return I18nControls.bindI18nProperties(new Text(), i18nKey);
    }

}
