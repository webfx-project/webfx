package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.collections.ListChangeListener;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRootBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg.SvgPathPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.PathPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.PathPeerMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class HtmlPathPeer
        <N extends Path, NB extends PathPeerBase<N, NB, NM>, NM extends PathPeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements PathPeerMixin<N, NB, NM> {

    private final SvgPathPeer svgPathPeer = new SvgPathPeer();

    public HtmlPathPeer() {
        this((NB) new PathPeerBase(), HtmlUtil.createElement("fx-path"));
    }

    public HtmlPathPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        // This peer is particular as we forward all the bindings to another peer (ie svgPathPeer).
        svgPathPeer.bind(node, sceneRequester); // So we actually bind the node to svgPathPeer (and not this instance).
        super.getNodePeerBase().setNode(node); // However, we tell this peer is associated with this node (necessary for correct pickPeer() behaviour).
        // But because we are in HTML (not SVG), we need to embed the svgPathPeer element (SVG path) in an SVG element.
        Element svgElement = SvgUtil.createSvgElement("svg");
        SvgRoot svgRoot = new SvgRootBase();
        node.getProperties().put("svgRoot", svgRoot);
        // Setting arbitrary large size to avoid the path to be cropped by the svg tag
        svgElement.setAttribute("width", 100_000);
        svgElement.setAttribute("height", 100_000);
        HtmlUtil.setChildren(svgElement, svgRoot.getDefsElement(), svgPathPeer.getElement());
        HtmlUtil.setChild(getElement(), svgElement);
    }

    @Override
    public NB getNodePeerBase() {
        return (NB) svgPathPeer.getNodePeerBase();
    }

    @Override
    public void updateFillRule(FillRule fillRule) {
        //svgPathPeer.updateFillRule(fillRule);
    }

    @Override
    public void updateElements(List<PathElement> elements, ListChangeListener.Change<PathElement> change) {
        //svgPathPeer.updateElements(elements, change);
    }
}
