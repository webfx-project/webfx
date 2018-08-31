package webfx.fx.spi.gwt.svg.peer;

import elemental2.dom.Element;
import webfx.fx.spi.gwt.util.SvgUtil;
import emul.javafx.scene.Group;
import webfx.fx.spi.peer.base.GroupPeerMixin;
import webfx.fx.spi.peer.base.GroupPeerBase;

/**
 * @author Bruno Salmon
 */
public class SvgGroupPeer
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends SvgNodePeer<N, NB, NM>
        implements GroupPeerMixin<N, NB, NM> {

    public SvgGroupPeer() {
        this((NB) new GroupPeerBase(), SvgUtil.createSvgGroup());
    }

    public SvgGroupPeer(NB base, Element element) {
        super(base, element);
    }
}
