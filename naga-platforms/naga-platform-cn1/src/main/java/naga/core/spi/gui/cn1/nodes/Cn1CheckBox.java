package naga.core.spi.gui.cn1.nodes;

import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.ToggleSwitch;


/**
 * @author Bruno Salmon
 */
public class Cn1CheckBox extends Cn1ButtonBase<com.codename1.ui.CheckBox> implements CheckBox<com.codename1.ui.CheckBox>, ToggleSwitch<com.codename1.ui.CheckBox> {

    public Cn1CheckBox() {
        this(new com.codename1.ui.CheckBox());
    }

    public Cn1CheckBox(com.codename1.ui.CheckBox button) {
        super(button);
    }
}

