package emul.com.sun.javafx.scene.control.skin;

import emul.com.sun.javafx.scene.control.behaviour.TextFieldBehavior;
import emul.javafx.scene.control.TextField;

/**
 * Text field skin.
 *
 * (empty as we rely on the target toolkit for now)
 */
public class TextFieldSkin extends TextInputControlSkin<TextField, TextFieldBehavior> {
    /**
     * Create a new TextFieldSkin.
     * @param textField not null
     */
    public TextFieldSkin(final TextField textField) {
        this(textField, /*(textField instanceof PasswordField)
                                     ? new PasswordFieldBehavior((PasswordField)textField)
                                     :*/ new TextFieldBehavior(textField));
    }

    public TextFieldSkin(final TextField textField, final TextFieldBehavior behavior) {
        super(textField, behavior);
    }
}

