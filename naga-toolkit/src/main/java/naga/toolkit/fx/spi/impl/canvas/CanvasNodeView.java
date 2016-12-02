package naga.toolkit.fx.spi.impl.canvas;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.spi.view.NodeView;

/**
 * @author Bruno Salmon
 */
public interface CanvasNodeView
        <N extends Node, CC>
        extends NodeView<N> {

    void prepareCanvasContext(CC c);

    void paint(CC c);

    boolean containsPoint(Point2D point);

}
