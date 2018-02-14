package naga.framework.ui.action;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import naga.framework.ui.graphic.image.JsonImageViews;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class ActionBuilder {

    private Object actionKey;

    private ObservableStringValue textProperty;
    private String text;
    private Object i18nKey;
    private I18n i18n;

    private ObservableObjectValue<Node> graphicProperty;
    private Node graphic;
    private Object graphicUrlOrJson;

    private ObservableBooleanValue disabledProperty;
    private ObservableBooleanValue visibleProperty;

    private boolean hiddenWhenDisabled;

    private boolean authRequired;

    private ObservableBooleanValue authorizedProperty;

    private EventHandler<ActionEvent> actionHandler;

    public ActionBuilder() {
    }

    public ActionBuilder(Object actionKey) {
        this.actionKey = actionKey;
    }

    public Object getActionKey() {
        return actionKey;
    }

    public ActionBuilder setActionKey(Object actionKey) {
        this.actionKey = actionKey;
        return this;
    }

    public ObservableStringValue getTextProperty() {
        return textProperty;
    }

    public ActionBuilder setTextProperty(ObservableStringValue textProperty) {
        this.textProperty = textProperty;
        return this;
    }

    public String getText() {
        return text;
    }

    public ActionBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public Object getI18nKey() {
        return i18nKey;
    }

    public ActionBuilder setI18nKey(Object i18nKey) {
        this.i18nKey = i18nKey;
        return this;
    }

    public I18n getI18n() {
        return i18n;
    }

    public ActionBuilder setI18n(I18n i18n) {
        this.i18n = i18n;
        return this;
    }

    public ObservableObjectValue<Node> getGraphicProperty() {
        return graphicProperty;
    }

    public ActionBuilder setGraphicProperty(ObservableObjectValue<Node> graphicProperty) {
        this.graphicProperty = graphicProperty;
        return this;
    }

    public Node getGraphic() {
        return graphic;
    }

    public ActionBuilder setGraphic(Node graphic) {
        this.graphic = graphic;
        return this;
    }

    public Object getGraphicUrlOrJson() {
        return graphicUrlOrJson;
    }

    public ActionBuilder setGraphicUrlOrJson(Object graphicUrlOrJson) {
        this.graphicUrlOrJson = graphicUrlOrJson;
        return this;
    }

    public ObservableBooleanValue getDisabledProperty() {
        return disabledProperty;
    }

    public ActionBuilder setDisabledProperty(ObservableBooleanValue disabledProperty) {
        this.disabledProperty = disabledProperty;
        return this;
    }

    public ObservableBooleanValue getVisibleProperty() {
        return visibleProperty;
    }

    public ActionBuilder setVisibleProperty(ObservableBooleanValue visibleProperty) {
        this.visibleProperty = visibleProperty;
        return this;
    }

    public boolean isHiddenWhenDisabled() {
        return hiddenWhenDisabled;
    }

    public ActionBuilder setHiddenWhenDisabled(boolean hiddenWhenDisabled) {
        this.hiddenWhenDisabled = hiddenWhenDisabled;
        return this;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public ActionBuilder setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
        return this;
    }

    public ObservableBooleanValue getAuthorizedProperty() {
        return authorizedProperty;
    }

    public ActionBuilder setAuthorizedProperty(ObservableBooleanValue authorizedProperty) {
        this.authorizedProperty = authorizedProperty;
        return this;
    }

    public EventHandler<ActionEvent> getActionHandler() {
        return actionHandler;
    }

    public ActionBuilder setActionHandler(EventHandler<ActionEvent> actionHandler) {
        this.actionHandler = actionHandler;
        return this;
    }

    public ActionBuilder setActionHandler(Runnable actionHandler) {
        return setActionHandler(e -> actionHandler.run());
    }

    public ActionBuilder register() {
        ActionRegistry.get().registerAction(this);
        return this;
    }

    public ActionBuilder duplicate() {
        return new ActionBuilder(actionKey)
                .setTextProperty(textProperty)
                .setText(text)
                .setI18nKey(i18nKey)
                .setI18n(i18n)
                .setGraphicProperty(graphicProperty)
                .setGraphic(graphic)
                .setGraphicUrlOrJson(graphicUrlOrJson)
                .setDisabledProperty(disabledProperty)
                .setVisibleProperty(visibleProperty)
                .setHiddenWhenDisabled(hiddenWhenDisabled)
                .setAuthRequired(authRequired)
                .setAuthorizedProperty(authorizedProperty)
                .setActionHandler(actionHandler)
                ;
    }

    public ActionBuilder removeText() {
        textProperty = null;
        text = null;
        i18nKey = null;
        return this;
    }

    public Action build() {
        if (textProperty == null) {
            if (i18nKey != null && i18n != null)
                textProperty = i18n.translationProperty(i18nKey);
            else
                textProperty = new SimpleStringProperty(text);
        }
        if (graphicProperty == null) {
            if (graphic == null && graphicUrlOrJson != null)
                graphic = JsonImageViews.createImageView(graphicUrlOrJson);
            graphicProperty = new SimpleObjectProperty<>(graphic);
        }
        if (disabledProperty == null) {
            if (authorizedProperty != null) {
                disabledProperty = BooleanExpression.booleanExpression(authorizedProperty).not();
                if (hiddenWhenDisabled && visibleProperty == null)
                    visibleProperty = authorizedProperty;
            } else
                disabledProperty = new SimpleBooleanProperty(authRequired);
        }
        if (visibleProperty == null) {
            if (hiddenWhenDisabled)
                visibleProperty = BooleanExpression.booleanExpression(disabledProperty).not();
            else
                visibleProperty = new SimpleBooleanProperty(true);
        }
        return Action.create(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler);
    }
}
