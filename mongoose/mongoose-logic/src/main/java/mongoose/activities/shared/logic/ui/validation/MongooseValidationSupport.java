package mongoose.activities.shared.logic.ui.validation;

import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import de.saxsys.mvvmfx.utils.validation.Validator;
import de.saxsys.mvvmfx.utils.validation.visualization.ControlsFxVisualizer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import naga.commons.util.collection.Collections;
import naga.framework.ui.controls.BackgroundUtil;
import naga.framework.ui.controls.BorderUtil;
import naga.fx.properties.Properties;
import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class MongooseValidationSupport {

    private static final Image ERROR_IMAGE = new Image("images/16/validation/decoration-error.png"); //$NON-NLS-1$
    protected static final Image REQUIRED_IMAGE = new Image("images/16/validation/required-indicator.png"); //$NON-NLS-1$

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
        addRequiredInput(textInputControl, "This field is required");
    }

    public void addRequiredInput(TextInputControl textInputControl, String errorMessage) {
        ObservableRuleBasedValidator validator = new ObservableRuleBasedValidator();
        ObservableBooleanValue rule = // ObservableRules.notEmpty(textInputControl.textProperty());
        Bindings.createBooleanBinding(() -> {
            if (!validatingProperty.get() || !isShowing(textInputControl))
                return true;
            final String s = textInputControl.getText();
            return s != null && !s.trim().isEmpty();
        }, textInputControl.textProperty(), validatingProperty);
        validator.addRule(rule, ValidationMessage.error(errorMessage));
        int index = validators.size();
        validators.add(validator);
        validatorErrorDecorationNodes.add(null);

        ControlsFxVisualizer validationVisualizer = new ControlsFxVisualizer();
        validationVisualizer.setDecoration(new GraphicValidationDecoration() {
            @Override
            protected Node createErrorNode() {
                Node errorNode = new ImageView(ERROR_IMAGE);
                validatorErrorDecorationNodes.set(index, errorNode);
                errorNode.getProperties().put("control", textInputControl);
                return errorNode;
            }

            @Override
            protected Collection<Decoration> createValidationDecorations(org.controlsfx.validation.ValidationMessage message) {
                return Arrays.asList(new GraphicDecoration(createDecorationNode(message), Pos.CENTER_RIGHT, 0, 0, -1, 0));
            }

            @Override
            protected Collection<Decoration> createRequiredDecorations(Control target) {
                return Arrays.asList(new GraphicDecoration(new ImageView(REQUIRED_IMAGE), Pos.CENTER_LEFT, -5, 0));
            }
        });
        validationVisualizer.initVisualization(validator.getValidationStatus(), textInputControl, true);
        textInputControl.getProperties().put("validationVisualizer", validationVisualizer);
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
            Rectangle triangle = new Rectangle(10, 10, Color.RED);
            triangle.getTransforms().add(new Rotate(45, 5, 5));
            triangle.layoutYProperty().bind(Properties.compute(label.heightProperty(), n -> n.doubleValue() - 7));
            triangle.setLayoutX(20d);
            popOverContentNode = new Group(label, triangle);
            //popOverContentNode.setOpacity(0.75);
            //popOverContentNode.setEffect(new DropShadow());
            showPopOver(errorDecorationNode);
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

    private void showPopOverNow() {
        Platform.runLater(() -> {
            if (popOverDecoration != null) {
                popOverDecoration.removeDecoration(popOverDecorationTarget);
                popOverDecoration = null;
            }
            if (isShowing(popOverOwnerNode)) {
                popOverDecorationTarget = (Node) popOverOwnerNode.getProperties().get("control");
                popOverDecoration = new GraphicDecoration(popOverContentNode, 0, -1, 0, -1);
                popOverDecoration.applyDecoration(popOverDecorationTarget);
            }
        });
    }

    private static boolean isShowing(Node node) {
        if (!node.isVisible())
            return false;
        if (node.getParent() != null)
            return isShowing(node.getParent());
        Scene scene = node.getScene();
        return scene != null && scene.getRoot() == node;
    }

}
