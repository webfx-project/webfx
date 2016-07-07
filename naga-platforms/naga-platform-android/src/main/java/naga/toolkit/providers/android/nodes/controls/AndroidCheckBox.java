package naga.toolkit.providers.android.nodes.controls;

import android.content.Context;
import naga.toolkit.providers.android.AndroidToolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;

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

