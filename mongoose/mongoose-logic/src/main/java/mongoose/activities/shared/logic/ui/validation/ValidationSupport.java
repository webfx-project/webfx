package mongoose.activities.shared.logic.ui.validation;

import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import de.saxsys.mvvmfx.utils.validation.Validator;
import de.saxsys.mvvmfx.utils.validation.visualization.ControlsFxVisualizer;
import de.saxsys.mvvmfx.utils.validation.visualization.ValidationVisualizer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import naga.commons.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ValidationSupport {

    private final List<Validator> validators = new ArrayList<>();
    private final BooleanProperty validatingProperty = new SimpleBooleanProperty();

    public boolean isValid() {
        validatingProperty.setValue(false);
        validatingProperty.setValue(true);
        return Collections.hasNoOneMatching(validators, validator -> !validator.getValidationStatus().isValid());
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
        ValidationVisualizer validationVisualizer = new ControlsFxVisualizer();
        validationVisualizer.initVisualization(validator.getValidationStatus(), textInputControl, true);
        textInputControl.getProperties().put("validationVisualizer", validationVisualizer);
        validators.add(validator);
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
