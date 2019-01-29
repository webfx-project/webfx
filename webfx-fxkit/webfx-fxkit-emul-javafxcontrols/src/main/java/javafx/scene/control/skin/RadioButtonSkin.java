package javafx.scene.control.skin;

import com.sun.javafx.scene.control.behaviour.RadioButtonBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.scene.control.RadioButton;

/**
 * Radio button skin.
 *
 * (empty as we rely on the target toolkit for now)
 */
public class RadioButtonSkin extends BehaviorSkinBase<RadioButton, RadioButtonBehavior> {

    public RadioButtonSkin(RadioButton radioButton) {
        this(radioButton, new RadioButtonBehavior(radioButton));
    }

    public RadioButtonSkin(RadioButton radioButton, RadioButtonBehavior behavior) {
        super(radioButton, behavior);
    }

}