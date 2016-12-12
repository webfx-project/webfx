package naga.toolkit.fx.scene.control;

import naga.toolkit.fx.scene.control.impl.ToggleButtonImpl;
import naga.toolkit.properties.markers.HasSelectedProperty;

/**
 * @author Bruno Salmon
 */
public interface ToggleButton extends ButtonBase,
        HasSelectedProperty {

    static ToggleButton create() {
        return new ToggleButtonImpl();
    }

}
