package javafx.scene.control.skin;

import com.sun.javafx.scene.control.behaviour.ToggleButtonBehavior;
import javafx.scene.control.ToggleButton;

public class ToggleButtonSkin extends LabeledSkinBase<ToggleButton, ToggleButtonBehavior<ToggleButton>> {

    public ToggleButtonSkin(ToggleButton toggleButton) {
        super(toggleButton, new ToggleButtonBehavior<>(toggleButton));
    }
}