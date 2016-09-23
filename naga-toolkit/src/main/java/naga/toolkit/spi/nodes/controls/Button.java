package naga.toolkit.spi.nodes.controls;

import naga.toolkit.properties.markers.HasActionEventObservable;
import naga.toolkit.properties.markers.HasImageProperty;

/**
 * @author Bruno Salmon
 */
public interface Button<N> extends ButtonBase<N>, HasImageProperty, HasActionEventObservable {
}
