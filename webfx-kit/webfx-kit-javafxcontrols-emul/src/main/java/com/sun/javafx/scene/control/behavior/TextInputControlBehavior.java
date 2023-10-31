package com.sun.javafx.scene.control.behavior;

import javafx.event.Event;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;

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
        // Although the key events are entirely managed by the peer, we consume them in JavaFX to not propagate these
        // events to further JavaFX controls.
        textInputControl.addEventHandler(KeyEvent.ANY, e -> {
            if (textInputControl.isFocused()) {
                // Exception is made for accelerators such as Enter or ESC, as they should be passed beyond this control
                switch (e.getCode()) {
                    case ENTER:
                    case ESCAPE:
                        return;
                }
                // Otherwise, we stop the propagation in JavaFX
                e.consume();
                // But we still ask WebFX to propagate them to the peer.
                Event.setPropagateToPeerEvent(e); // See WebFX comments on Event class for more explanation.
            }
        });
    }

}