package naga.core.spi.toolkit.android.controls;

import android.content.Context;
import naga.core.spi.toolkit.android.AndroidToolkit;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.ToggleSwitch;

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

