package webfx.fxkit.javafxgraphics.mapper.highcoupling.spi;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;

/**
 * @author Bruno Salmon
 */
public interface CanvasNodePeer
        <N extends Node, CC>
        extends NodePeer<N> {

    void prepareCanvasContext(CC c);

    void paint(CC c);

    boolean containsPoint(Point2D point);

}
