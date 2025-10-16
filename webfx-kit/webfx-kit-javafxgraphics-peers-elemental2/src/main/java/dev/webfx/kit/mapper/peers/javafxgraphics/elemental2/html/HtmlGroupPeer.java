package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.dom.HTMLElement;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import javafx.scene.Group;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.GroupPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.GroupPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlGroupPeer
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements GroupPeerMixin<N, NB, NM> {

    public HtmlGroupPeer() {
        this("fx-group");
    }

    public HtmlGroupPeer(String tagName) {
        this((NB) new GroupPeerBase(), HtmlUtil.createElement(tagName));
    }

    public HtmlGroupPeer(NB base, HTMLElement element) {
        super(base, element);
    }
}
