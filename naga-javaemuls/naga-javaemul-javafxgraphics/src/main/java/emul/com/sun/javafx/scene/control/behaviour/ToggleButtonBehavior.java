package emul.com.sun.javafx.scene.control.behaviour;

import emul.javafx.collections.ObservableList;
import emul.javafx.geometry.NodeOrientation;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.Control;
import emul.javafx.scene.control.Toggle;
import emul.javafx.scene.control.ToggleButton;
import emul.javafx.scene.control.ToggleGroup;
import emul.javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class ToggleButtonBehavior<C extends ToggleButton> extends ButtonBehavior<C>{

    public ToggleButtonBehavior(C button) {
        super(button, TOGGLE_BUTTON_BINDINGS);
    }

    /**
     * The key bindings for the ToggleButton. Sets up the keys to open the menu.
     */
    protected static final List<KeyBinding> TOGGLE_BUTTON_BINDINGS = new ArrayList<>();
    static {
        TOGGLE_BUTTON_BINDINGS.addAll(BUTTON_BINDINGS);
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ToggleNext-Right"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TogglePrevious-Left"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "ToggleNext-Down"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.UP, "TogglePrevious-Up"));
    }

    /**
     * Returns the next toggle index or "from" if none found
     */
    private int nextToggleIndex(final ObservableList<Toggle> toggles, final int from) {
        Toggle toggle;
        if (from  < 0 || from >= toggles.size()) return 0;
        int i = (from + 1) % toggles.size();
        while (i != from && (toggle = toggles.get(i)) instanceof Node &&
                ((Node)toggle).isDisabled()) {
            i = (i + 1) % toggles.size();
        }
        return i;
    }

    /**
     * Returns the previous toggle index or "from" if none found
     */
    private int previousToggleIndex(final ObservableList<Toggle> toggles, final int from) {
        Toggle toggle;
        if (from  < 0 || from >= toggles.size()) return toggles.size();
        int i = Math.floorMod(from - 1, toggles.size());
        while (i != from && (toggle = toggles.get(i)) instanceof Node &&
                ((Node)toggle).isDisabled()) {
            i = Math.floorMod(i - 1, toggles.size());
        }
        return i;
    }

    @Override
    protected void callAction(String name) {
        ToggleButton toggleButton = getControl();
        final ToggleGroup toggleGroup = toggleButton.getToggleGroup();
        // A ToggleButton does not have to be in a group.
        if (toggleGroup == null) {
            super.callAction(name);
            return;
        }
        ObservableList<Toggle> toggles = toggleGroup.getToggles();
        final int currentToggleIdx = toggles.indexOf(toggleButton);

        switch (name) {
            case "ToggleNext-Right":
            case "ToggleNext-Down":
            case "TogglePrevious-Left":
            case "TogglePrevious-Up":
                boolean traversingToNext = traversingToNext(name, null /*toggleButton.getEffectiveNodeOrientation()*/);
                /*if (Utils.isTwoLevelFocus()) {
                    super.callAction(toggleToTraverseAction(name));
                } else*/ if (traversingToNext) {
                    int nextToggleIndex = nextToggleIndex(toggles, currentToggleIdx);
                    if (nextToggleIndex == currentToggleIdx) {
                        super.callAction(toggleToTraverseAction(name));
                    } else {
                        Toggle toggle = toggles.get(nextToggleIndex);
                        toggleGroup.selectToggle(toggle);
                        ((Control)toggle).requestFocus();
                    }
                } else {
                    int prevToggleIndex = previousToggleIndex(toggles, currentToggleIdx);
                    if (prevToggleIndex == currentToggleIdx) {
                        super.callAction(toggleToTraverseAction(name));
                    } else {
                        Toggle toggle = toggles.get(prevToggleIndex);
                        toggleGroup.selectToggle(toggle);
                        ((Control)toggle).requestFocus();
                    }
                }
                break;
            default: super.callAction(name);
        }
    }

    private boolean traversingToNext(String name, NodeOrientation effectiveNodeOrientation) {
        boolean rtl = effectiveNodeOrientation == NodeOrientation.RIGHT_TO_LEFT;
        switch (name) {
            case "ToggleNext-Right":
                return rtl ? false : true;
            case "ToggleNext-Down":
                return true;
            case "TogglePrevious-Left":
                return rtl ? true : false;
            case "TogglePrevious-Up":
                return false;
            default:
                throw new IllegalArgumentException("Not a toggle action");
        }
    }

    private String toggleToTraverseAction(String name) {
        switch (name) {
            case "ToggleNext-Right":
                return TRAVERSE_RIGHT;
            case "ToggleNext-Down":
                return TRAVERSE_DOWN;
            case "TogglePrevious-Left":
                return TRAVERSE_LEFT;
            case "TogglePrevious-Up":
                return TRAVERSE_UP;
            default:
                throw new IllegalArgumentException("Not a toggle action");
        }
    }
}

