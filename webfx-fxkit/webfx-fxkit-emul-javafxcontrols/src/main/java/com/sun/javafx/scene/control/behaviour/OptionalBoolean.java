package com.sun.javafx.scene.control.behaviour;

/**
 * A tri-state boolean used with KeyBinding.
 */
public enum OptionalBoolean {
    TRUE,
    FALSE,
    ANY;

    public boolean equals(boolean b) {
        if (this == ANY) return true;
        if (b && this == TRUE) return true;
        if (!b && this == FALSE) return true;
        return false;
    }
}
