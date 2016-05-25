package naga.core.spi.toolkit.gwt.nodes;

import naga.core.spi.toolkit.nodes.CheckBox;
import naga.core.spi.toolkit.nodes.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtCheckBox extends GwtSelectableButton<com.google.gwt.user.client.ui.CheckBox> implements CheckBox<com.google.gwt.user.client.ui.CheckBox>, ToggleSwitch<com.google.gwt.user.client.ui.CheckBox> {

    public GwtCheckBox() {
        this(new com.google.gwt.user.client.ui.CheckBox());
    }

    public GwtCheckBox(com.google.gwt.user.client.ui.CheckBox button) {
        super(button);
    }
}
