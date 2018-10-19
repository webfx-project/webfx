package webfx.framework.client.ui.action;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;
import webfx.framework.client.ui.action.impl.WritableAction;
import webfx.fxkit.util.properties.ObservableLists;
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

    private static void bindLabeledToAction(Labeled labeled, Action action) {
        labeled.textProperty().bind(action.textProperty());
        labeled.graphicProperty().bind(action.graphicProperty());
        bindNodeToAction(labeled, action, false);
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
