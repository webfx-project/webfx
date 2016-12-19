package naga.toolkit.fx.scene;

import naga.toolkit.fx.spi.viewer.NodeViewerFactory;
import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasRootProperty;
import naga.toolkit.properties.markers.HasWidthProperty;

/**
 * @author Bruno Salmon
 */
public interface Scene extends
        HasRootProperty,
        HasWidthProperty,
        HasHeightProperty {

    void setNodeViewerFactory(NodeViewerFactory nodeViewerFactory);

}
