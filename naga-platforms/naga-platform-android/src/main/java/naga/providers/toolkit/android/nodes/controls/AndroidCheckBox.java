package naga.providers.toolkit.android.nodes.controls;

import android.content.Context;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;
import naga.providers.toolkit.android.AndroidToolkit;

/**
 * @author Bruno Salmon
 */
public class AndroidCheckBox extends AndroidButtonBase<android.widget.CheckBox> implements naga.toolkit.spi.nodes.controls.CheckBox<android.widget.CheckBox>, ToggleSwitch<android.widget.CheckBox> {

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

