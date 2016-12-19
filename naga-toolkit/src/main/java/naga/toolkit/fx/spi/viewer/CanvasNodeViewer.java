package naga.toolkit.fx.spi.viewer;

import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface CanvasNodeViewer
        <N extends Node, CC>
        extends NodeViewer<N> {

    void prepareCanvasContext(CC c);

    void paint(CC c);

    boolean containsPoint(Point2D point);

}
