package dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;

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
