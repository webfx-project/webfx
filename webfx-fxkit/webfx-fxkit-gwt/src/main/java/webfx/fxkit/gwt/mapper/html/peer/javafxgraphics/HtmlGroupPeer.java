package webfx.fxkit.gwt.mapper.html.peer.javafxgraphics;

import elemental2.dom.HTMLElement;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import javafx.scene.Group;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.GroupPeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.GroupPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlGroupPeer
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements GroupPeerMixin<N, NB, NM> {

    public HtmlGroupPeer() {
        this((NB) new GroupPeerBase(), HtmlUtil.createDivElement());
    }

    public HtmlGroupPeer(NB base, HTMLElement element) {
        super(base, element);
    }
}
