package naga.toolkit.drawing.shapes;

import naga.toolkit.properties.markers.HasFillProperty;
import naga.toolkit.properties.markers.HasStrokeLineCapProperty;
import naga.toolkit.properties.markers.HasStrokeProperty;
import naga.toolkit.properties.markers.HasStrokeWidthProperty;

/**
 * @author Bruno Salmon
 */
public interface Shape extends HasFillProperty, HasStrokeProperty, HasStrokeWidthProperty, HasStrokeLineCapProperty {
}
