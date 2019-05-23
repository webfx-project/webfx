package com.sun.javafx.scene.control.behaviour;

import javafx.scene.control.TextInputControl;

import java.util.List;

/**
 * Abstract base class for text input behaviors.
 *
 * (empty as we rely on the target toolkit for now)
 */
public abstract class TextInputControlBehavior<T extends TextInputControl> extends BehaviorBase<T> {

    /**************************************************************************
     * Constructors                                                           *
     *************************************************************************/

    /**
     * Create a new TextInputControlBehavior.
     * @param textInputControl cannot be null
     */
    public TextInputControlBehavior(T textInputControl, List<KeyBinding> bindings) {
        super(textInputControl, bindings);
    }

}