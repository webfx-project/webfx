package emul.com.sun.javafx.scene.control.skin;

import emul.com.sun.javafx.scene.control.behaviour.RadioButtonBehavior;
import emul.javafx.scene.control.RadioButton;

/**
 * Radio button skin.
 *
 * (empty as we rely on the target toolkit for now)
 */
public class RadioButtonSkin extends BehaviorSkinBase<RadioButton, RadioButtonBehavior> {

    public RadioButtonSkin(final RadioButton textField) {
        this(textField, new RadioButtonBehavior(textField));
    }

    public RadioButtonSkin(RadioButton radioButton, RadioButtonBehavior behavior) {
        super(radioButton, behavior);
    }

}