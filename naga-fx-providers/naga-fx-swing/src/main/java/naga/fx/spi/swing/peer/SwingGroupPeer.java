package naga.fx.spi.swing.peer;

import naga.fx.spi.peer.base.GroupPeerBase;
import naga.fx.spi.peer.base.GroupPeerMixin;
import emul.com.sun.javafx.geom.Point2D;
import emul.javafx.scene.Group;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingGroupPeer
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends SwingNodePeer<N, NB, NM>
        implements GroupPeerMixin<N, NB, NM> {

    public SwingGroupPeer() {
        this((NB) new GroupPeerBase());
    }

    public SwingGroupPeer(NB base) {
        super(base);
    }

    @Override
    public void paint(Graphics2D c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }
}
