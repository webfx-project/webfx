package mongoose.client.validation;

import webfx.framework.client.ui.validation.mvvmfx.ObservableRuleBasedValidator;
import webfx.framework.client.ui.validation.mvvmfx.ValidationMessage;
import webfx.framework.client.ui.validation.mvvmfx.Validator;
import webfx.framework.client.ui.validation.mvvmfx.visualization.ControlsFxVisualizer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import webfx.framework.client.ui.validation.controlsfx.control.decoration.Decoration;
import webfx.framework.client.ui.validation.controlsfx.control.decoration.GraphicDecoration;
import webfx.framework.client.ui.validation.controlsfx.validation.decoration.GraphicValidationDecoration;
import webfx.framework.client.ui.util.background.BackgroundUtil;
import webfx.framework.client.ui.util.border.BorderUtil;
import webfx.framework.client.ui.util.scene.SceneUtil;
import webfx.kit.util.properties.Properties;
import webfx.extras.imagestore.ImageStore;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class MongooseValidationSupport {

    private static final String DEFAULT_REQUIRED_MESSAGE = "This field is required";

    private final List<Validator> validators = new ArrayList<>();
    private final List<Node> validatorErrorDecorationNodes = new ArrayList<>();
    private final BooleanProperty validatingProperty = new SimpleBooleanProperty();
    private Node popOverContentNode;
    private Node popOverOwnerNode;

    public boolean isValid() {
        validatingProperty.setValue(false);
        validatingProperty.setValue(true);
        Validator firstInvalidValidator = firstInvalidValidator();
        if (firstInvalidValidator != null)
            Platform.runLater(() -> {
                popUpOverAutoScroll = true;
                showValidatorErrorPopOver(firstInvalidValidator);
            });
        return firstInvalidValidator == null;
    }

    private Validator firstInvalidValidator() {
        return Collections.findFirst(validators, validator -> !validator.getValidationStatus().isValid());
    }

    public void addRequiredInputs(TextInputControl... textInputControls) {
        for (TextInputControl textInputControl : textInputControls)
            addRequiredInput(textInputControl);
    }

    public void addRequiredInput(TextInputControl textInputControl) {
        addRequiredInput(textInputControl, DEFAULT_REQUIRED_MESSAGE);
    }

    public void addRequiredInput(TextInputControl textInputControl, String errorMessage) {
        addRequiredInput(textInputControl.textProperty(), textInputControl, errorMessage);
    }

    public void addRequiredInput(ObservableValue valueProperty, Node inputNode) {
        addRequiredInput(valueProperty, inputNode, DEFAULT_REQUIRED_MESSAGE);
    }

    public void addRequiredInput(ObservableValue valueProperty, Node inputNode, String errorMessage) {
        addValidationRule(Bindings.createBooleanBinding(() -> testNotEmpty(valueProperty.getValue()), valueProperty), inputNode, errorMessage);
    }

    private static boolean testNotEmpty(Object value) {
        return value != null && (!(value instanceof String) || !((String) value).trim().isEmpty());
    }

    public void addValidationRule(ObservableValue<Boolean> validProperty, Node node, String errorMessage) {
        ObservableRuleBasedValidator validator = new ObservableRuleBasedValidator();
        ObservableBooleanValue rule =
                Bindings.createBooleanBinding(() ->
                    !validatingProperty.get() || validProperty.getValue() || !isShowing(node)
                , validProperty, validatingProperty);
        validator.addRule(rule, ValidationMessage.error(errorMessage));
        validators.add(validator);
        validatorErrorDecorationNodes.add(node);

        if (node instanceof Control) {
            Control control = (Control) node;
            ControlsFxVisualizer validationVisualizer = new ControlsFxVisualizer();
            validationVisualizer.setDecoration(new GraphicValidationDecoration() {
                @Override
                protected Node createErrorNode() {
                    return ImageStore.createImageView(MongooseValidationIcons.validationErrorIcon16Url);
                }

                @Override
                protected Collection<Decoration> createValidationDecorations(webfx.framework.client.ui.validation.controlsfx.validation.ValidationMessage message) {
                    boolean isTextInput = node instanceof TextInputControl;
                    boolean isButton = node instanceof Button;
                    // isInside flag will determine if we position the decoration inside the node or not (ie outside)
                    boolean isInside;
                    if (isTextInput) // inside for text inputs
                        isInside = true;
                    else { // for others, will be generally outside unless it is stretched to full width by its container
                        Parent parent = node.getParent();
                        while (parent instanceof Pane && !(parent instanceof VBox) && !(parent instanceof HBox))
                            parent = parent.getParent();
                        isInside = parent instanceof VBox && ((VBox) parent).isFillWidth();
                    }
                    double xRelativeOffset = isInside ? -1 : 1; // positioning the decoration inside the control for button and text input
                    double xOffset = isInside && isButton ?  -20 : 0; // moving the decoration before the drop down arrow
                    return java.util.Collections.singletonList(
                            new GraphicDecoration(createDecorationNode(message),
                                    Pos.CENTER_RIGHT,
                                    xOffset,
                                    0,
                                    xRelativeOffset,
                                    0)
                    );
                }

                @Override
                protected Collection<Decoration> createRequiredDecorations(Control target) {
                    return java.util.Collections.singletonList(
                            new GraphicDecoration(ImageStore.createImageView(MongooseValidationIcons.validationRequiredIcon16Url),
                                    Pos.CENTER_LEFT,
                                    -10,
                                    0));
                }
            });
            validationVisualizer.initVisualization(validator.getValidationStatus(), control, true);
            node.getProperties().put("validationVisualizer", validationVisualizer);
        }
    }

    private void showValidatorErrorPopOver(Validator validator) {
        int index = validators.indexOf(validator);
        if (index >= 0) {
            Node decorationNode = validatorErrorDecorationNodes.get(index);
            if (decorationNode != null)
                showValidatorErrorPopOver(validator, decorationNode);
        }
    }

    private void showValidatorErrorPopOver(Validator validator, Node errorDecorationNode) {
        ValidationMessage errorMessage = Collections.first(validator.getValidationStatus().getErrorMessages());
        if (errorMessage != null) {
            Label label = new Label(errorMessage.getMessage());
            label.setPadding(new Insets(8));
            label.setFont(Font.font("Verdana", 11.5));
            label.setTextFill(Color.WHITE);
            label.setBackground(BackgroundUtil.newBackground(Color.RED, 5, 2));
            label.setBorder(BorderUtil.newBorder(Color.WHITE, 5, 2));
            Rectangle diamond = new Rectangle(10, 10, Color.RED);
            diamond.getTransforms().add(new Rotate(45, 5, 5));
            diamond.layoutYProperty().bind(Properties.compute(label.heightProperty(), n -> n.doubleValue() - 7));
            diamond.setLayoutX(20d);
            popOverContentNode = new Group(label, diamond);
            //popOverContentNode.setOpacity(0.75);
            //popOverContentNode.setEffect(new DropShadow());
            showPopOver(errorDecorationNode);
            // Removing the error pop over when the status is valid again
            validator.getValidationStatus().validProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean valid) {
                    if (valid) {
                        observable.removeListener(this);
                        popOverOwnerNode = null;
                        hidePopOver();
                    }
                }
            });
        }
    }

    private void showPopOver(Node node) {
        popOverOwnerNode = node;
        showPopOverNow();
        if (!node.getProperties().containsKey("popOverListen")) {
            node.getProperties().put("popOverListen", true);
            node.sceneProperty().addListener(observable -> {
                if (popOverOwnerNode == node) {
                    showPopOverNow();
                }
            });
            node.parentProperty().addListener(observable -> {
                if (popOverOwnerNode == node) {
                    showPopOverNow();
                }
            });
        }
    }

/*
    private PopOver popOver;
    private void showPopOverNow() {
        if (popOver != null && popOver.getOwnerNode() != popOverOwnerNode) {
            popOver.hide();
            popOver = null;
        }
        if (popOver == null && isShowing(popOverOwnerNode)) {
            popOver = new PopOver();
            popOver.setContentNode(popOverContentNode);
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
            //Platform.runLater(() -> {
                popOver.show(popOverOwnerNode, -(popOverOwnerNode instanceof ImageView ? ((ImageView) popOverOwnerNode).getImage().getHeight() : 0) + 4);
            //});
        }
    }
*/

    private GraphicDecoration popOverDecoration;
    private Node popOverDecorationTarget;
    private boolean popUpOverAutoScroll;

    private void showPopOverNow() {
        Platform.runLater(() -> {
            hidePopOver();
            if (isShowing(popOverOwnerNode)) {
                popOverDecorationTarget = popOverOwnerNode;
                popOverDecoration = new GraphicDecoration(popOverContentNode, 0, -1, 0, -1);
                popOverDecoration.applyDecoration(popOverDecorationTarget);
                if (popUpOverAutoScroll) {
                    SceneUtil.scrollNodeToBeVerticallyVisibleOnScene(popOverDecorationTarget);
                    popUpOverAutoScroll = false;
                }
            }
        });
    }

    private void hidePopOver() {
        UiScheduler.runInUiThread(() -> {
            if (popOverDecoration != null) {
                popOverDecoration.removeDecoration(popOverDecorationTarget);
                popOverDecoration = null;
            }
        });
    }

    private static boolean isShowing(Node node) {
        if (!node.isVisible())
            return false;
        Parent parent = node.getParent();
        if (parent != null)
            return isShowing(parent);
        Scene scene = node.getScene();
        return scene != null && scene.getRoot() == node;
    }

}
