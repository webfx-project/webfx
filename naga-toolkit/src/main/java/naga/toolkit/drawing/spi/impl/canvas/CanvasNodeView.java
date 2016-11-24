package naga.toolkit.drawing.spi.impl.canvas;

import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.drawing.spi.view.NodeView;

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
