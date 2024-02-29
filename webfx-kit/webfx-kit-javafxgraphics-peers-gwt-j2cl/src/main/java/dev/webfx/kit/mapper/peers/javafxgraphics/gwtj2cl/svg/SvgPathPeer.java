package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.svg;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.PathPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.PathPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.SvgUtil;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.Element;
import javafx.collections.ListChangeListener;
import javafx.scene.shape.*;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class SvgPathPeer
        <N extends Path, NB extends PathPeerBase<N, NB, NM>, NM extends PathPeerMixin<N, NB, NM>>

        extends SvgShapePeer<N, NB, NM>
        implements PathPeerMixin<N, NB, NM> {

    public SvgPathPeer() {
        this((NB) new PathPeerBase(), SvgUtil.createSvgPath());
    }

    public SvgPathPeer(NB base, Element element) {
        super(base, element);
    }

    @Override
    public void updateFillRule(FillRule fillRule) {
        setElementAttribute("fill-rule", fillRule == FillRule.EVEN_ODD ? "evenodd" : fillRule == FillRule.NON_ZERO ? "nonzero" : null);
    }

    public void updatePath(String path) {
        setElementAttribute("d", path);
    }

    @Override
    public void updateElements(List<PathElement> elements, ListChangeListener.Change<PathElement> change) {
        updatePath(toSvgPathData(elements));
        updateAllNodeTransforms(getNode().getAllNodeTransforms());
    }

    private String toSvgPathData(List<PathElement> elements) {
        StringBuilder sb = new StringBuilder();
        Collections.forEach(elements, e -> e.appendToSvgPath(sb));
        return sb.toString();
    }

}
