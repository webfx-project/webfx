package naga.fx.spi.viewer;

import naga.fx.geom.Point2D;
import naga.fx.scene.Node;

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
