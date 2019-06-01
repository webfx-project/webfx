package javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.scene.control.TextInputControl;

/**
 * Abstract base class for text input skins.
 *
 * (empty as we rely on the target toolkit for now)
 */
public abstract class TextInputControlSkin<T extends TextInputControl, B extends TextInputControlBehavior<T>> extends BehaviorSkinBase<T, B> {


    public TextInputControlSkin(final T textInput, final B behavior) {
        super(textInput, behavior);
    }

}