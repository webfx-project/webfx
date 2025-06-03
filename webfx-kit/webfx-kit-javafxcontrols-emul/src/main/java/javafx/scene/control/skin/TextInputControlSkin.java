package javafx.scene.control.skin;

import javafx.scene.control.SkinBase;
import javafx.scene.control.TextInputControl;

/**
 * Abstract base class for text input skins.
 *
 * (empty as we rely on the target toolkit for now)
 */
public abstract class TextInputControlSkin<T extends TextInputControl> extends SkinBase<T> {

    public TextInputControlSkin(final T textInput) {
        super(textInput);
    }

}