package naga.core.spi.gui.android.nodes;

import android.content.Context;
import naga.core.spi.gui.android.AndroidToolkit;
import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class AndroidCheckBox extends AndroidButtonBase<android.widget.CheckBox> implements CheckBox<android.widget.CheckBox>, ToggleSwitch<android.widget.CheckBox> {

    public AndroidCheckBox() {
        this(AndroidToolkit.currentActivity);
    }

    public AndroidCheckBox(Context context) {
        this(new android.widget.CheckBox(context));
    }

    public AndroidCheckBox(android.widget.CheckBox button) {
        super(button);
    }
}

