package naga.core.spi.gui.gwt.nodes;

import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtCheckBox extends GwtButtonBase<com.google.gwt.user.client.ui.CheckBox> implements CheckBox<com.google.gwt.user.client.ui.CheckBox>, ToggleSwitch<com.google.gwt.user.client.ui.CheckBox> {

    public GwtCheckBox() {
        this(new com.google.gwt.user.client.ui.CheckBox());
    }

    public GwtCheckBox(com.google.gwt.user.client.ui.CheckBox button) {
        super(button);
    }
}
