package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.HTMLElement;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import javafx.scene.Group;
import webfx.kit.mapper.peers.javafxgraphics.base.GroupPeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.GroupPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlGroupPeer
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements GroupPeerMixin<N, NB, NM> {

    public HtmlGroupPeer() {
        this((NB) new GroupPeerBase(), HtmlUtil.createElement("fx-group"));
    }

    public HtmlGroupPeer(NB base, HTMLElement element) {
        super(base, element);
    }
}
