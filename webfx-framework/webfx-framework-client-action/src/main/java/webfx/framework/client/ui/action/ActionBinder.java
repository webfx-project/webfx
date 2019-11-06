package webfx.framework.client.ui.action;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import webfx.framework.client.ui.action.impl.WritableAction;
import webfx.kit.util.properties.ObservableLists;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.util.function.Converter;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class ActionBinder {

    public static void bindButtonToAction(Button button, Action action) {
        bindLabeledToAction(button, action);
        button.setOnAction(action);
    }

    public static void bindMenuItemToAction(MenuItem menuItem, Action action) {
        menuItem.textProperty().bind(action.textProperty());
        bindGraphicProperties(menuItem.graphicProperty(), action.graphicProperty());
        menuItem.disableProperty().bind(action.disabledProperty());
        menuItem.visibleProperty().bind(action.visibleProperty());
        menuItem.setOnAction(action);
    }

    private static void bindLabeledToAction(Labeled labeled, Action action) {
        labeled.textProperty().bind(action.textProperty());
        bindGraphicProperties(labeled.graphicProperty(), action.graphicProperty());
        bindNodeToAction(labeled, action, false);
    }

    private static void bindGraphicProperties(ObjectProperty<Node> dstGraphicProperty, ObservableObjectValue<Node> srcGraphicProperty) {
        // Needs to make a copy of the graphic in case it is used in several places (JavaFx nodes must be unique instances in the scene graph)
        Properties.runNowAndOnPropertiesChange(p -> dstGraphicProperty.setValue(copyGraphic(srcGraphicProperty.getValue())), srcGraphicProperty);
    }

    private static Node copyGraphic(Node graphic) {
        // Handling only ImageView for now
        if (graphic instanceof ImageView) {
            ImageView imageView = (ImageView) graphic;
            ImageView copy = new ImageView();
            copy.imageProperty().bind(imageView.imageProperty());
            copy.fitWidthProperty().bind(imageView.fitWidthProperty());
            copy.fitHeightProperty().bind(imageView.fitHeightProperty());
            return copy;
        }
        return graphic;
    }

    public static Node getAndBindActionIcon(Action action) {
        Node icon = action.getGraphic();
        bindNodeToAction(icon, action, true);
        return icon;
    }

    private static void bindNodeToAction(Node node, Action action, boolean setOnMouseClicked) {
        node.disableProperty().bind(action.disabledProperty());
        node.visibleProperty().bind(action.visibleProperty());
        if (setOnMouseClicked)
            node.setOnMouseClicked(e -> action.handle(new ActionEvent(e.getSource(), e.getTarget())));
    }

    public static void bindWritableActionToAction(WritableAction writableAction, Action action) {
        writableAction.writableTextProperty().bind(action.textProperty());
        writableAction.writableGraphicProperty().bind(action.graphicProperty());
        writableAction.writableDisabledProperty().bind(action.disabledProperty());
        writableAction.writableVisibleProperty().bind(action.visibleProperty());
    }

    public static <P extends Pane> P bindChildrenToVisibleActions(P parent, Collection<Action> actions, Converter<Action, Node> nodeFactory) {
        ActionGroup actionGroup = new ActionGroupBuilder().setActions(actions).build();
        return bindChildrenToActionGroup(parent, actionGroup, nodeFactory);
    }

    public static <P extends Pane> P bindChildrenToActionGroup(P parent, ActionGroup actionGroup, Converter<Action, Node> nodeFactory) {
        bindChildrenToActionGroup(parent.getChildren(), actionGroup, nodeFactory);
        return parent;
    }

    public static void bindChildrenToActionGroup(ObservableList<Node> children, ActionGroup actionGroup, Converter<Action, Node> nodeFactory) {
        ObservableLists.bindConverted(children, actionGroup.getVisibleActions(), nodeFactory);
    }
}
