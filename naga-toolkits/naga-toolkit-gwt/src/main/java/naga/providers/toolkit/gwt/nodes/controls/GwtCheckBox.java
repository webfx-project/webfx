package naga.providers.toolkit.gwt.nodes.controls;

import naga.toolkit.spi.nodes.controls.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtCheckBox extends GwtSelectableButton<com.google.gwt.user.client.ui.CheckBox> implements naga.toolkit.spi.nodes.controls.CheckBox, ToggleSwitch {

    public GwtCheckBox() {
        this(new com.google.gwt.user.client.ui.CheckBox());
    }

    public GwtCheckBox(com.google.gwt.user.client.ui.CheckBox button) {
        super(button);
    }
}
