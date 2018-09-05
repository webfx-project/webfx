package webfx.fxkits.core.spi.peer;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;

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
