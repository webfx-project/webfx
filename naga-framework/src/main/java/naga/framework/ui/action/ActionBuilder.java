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

    private ObservableObjectValue<Node> graphicProperty;
    private Node graphic;
    private Object graphicUrlOrJson;

    private ObservableBooleanValue disabledProperty;
    private ObservableBooleanValue visibleProperty;

    private boolean hiddenWhenDisabled;

    private boolean authRequired;

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

    public ActionBuilder register() {
        ActionRegistry.registerAction(this);
        return this;
    }

    public Action build(EventHandler<ActionEvent> actionHandler, ObservableBooleanValue authorizedProperty, I18n i18n) {
        ObservableStringValue textProperty;
        if (getTextProperty() != null)
            textProperty = getTextProperty();
        else if (getI18nKey() != null && i18n != null)
            textProperty = i18n.translationProperty(getI18nKey());
        else
            textProperty = new SimpleStringProperty(getText());
        ObservableObjectValue<Node> graphicProperty;
        if (getGraphicProperty() != null)
            graphicProperty = getGraphicProperty();
        else {
            Node graphic = getGraphic();
            if (graphic == null && getGraphicUrlOrJson() != null)
                graphic = JsonImageViews.createImageView(getGraphicUrlOrJson());
            graphicProperty = new SimpleObjectProperty<>(graphic);
        }
        ObservableBooleanValue disabledProperty;
        if (getDisabledProperty() != null)
            disabledProperty = getDisabledProperty();
        else if (authorizedProperty != null)
            disabledProperty = BooleanExpression.booleanExpression(authorizedProperty).not();
        else
            disabledProperty = new SimpleBooleanProperty(isAuthRequired());
        ObservableBooleanValue visibleProperty;
        if (getVisibleProperty() != null)
            visibleProperty = getVisibleProperty();
        else if (isHiddenWhenDisabled())
            visibleProperty = getDisabledProperty() == null && authorizedProperty != null ? authorizedProperty : BooleanExpression.booleanExpression(disabledProperty).not();
        else
            visibleProperty = new SimpleBooleanProperty(true);
        return Action.create(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler);
    }
}
