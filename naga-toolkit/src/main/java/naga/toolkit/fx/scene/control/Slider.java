package naga.toolkit.fx.scene.control;

import naga.toolkit.fx.scene.control.impl.SliderImpl;
import naga.toolkit.properties.markers.HasMaxProperty;
import naga.toolkit.properties.markers.HasMinProperty;
import naga.toolkit.properties.markers.HasValueProperty;

/**
 * @author Bruno Salmon
 */
public interface Slider extends Control,
        HasMinProperty,
        HasValueProperty,
        HasMaxProperty {

    static Slider create() {
        return new SliderImpl();
    }
}
