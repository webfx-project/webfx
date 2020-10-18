package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import webfx.kit.mapper.peers.javafxgraphics.base.SVGPathPeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.SVGPathPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRoot;
import webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRootBase;
import webfx.kit.mapper.peers.javafxgraphics.gwt.svg.SvgPathPeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlSVGPathPeer
        <N extends SVGPath, NB extends SVGPathPeerBase<N, NB, NM>, NM extends SVGPathPeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements SVGPathPeerMixin<N, NB, NM> {

    private final SvgPathPeer svgPathPeer = new SvgPathPeer();

    public HtmlSVGPathPeer() {
        this((NB) new SVGPathPeerBase(), HtmlUtil.createElement("fx-svgpath"));
    }

    public HtmlSVGPathPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        Element svgElement = SvgUtil.createSvgElement("svg");
        SvgRoot svgRoot = new SvgRootBase();
        node.getProperties().put("svgRoot", svgRoot);
        svgPathPeer.getNodePeerBase().setNode(node); // Necessary, otherwise NPE when fill is a gradient
        // Hack used by Mandelbrot demo TODO: see if we can automatically compute the size from the path element
        Object ms = node.getProperties().get("webfx-svgpath-maxSize");
        if (ms instanceof Number) {
            double maxSize = ((Number) ms).doubleValue();
            svgElement.setAttribute("width",  maxSize);
            svgElement.setAttribute("height", maxSize);
        }
        HtmlUtil.setChildren(svgElement, svgRoot.getDefsElement(), svgPathPeer.getElement());
        HtmlUtil.setChild(getElement(), svgElement);
    }

    @Override
    public void updateFill(Paint fill) {
        svgPathPeer.updateFill(fill);
    }

    @Override
    public void updateFillRule(FillRule fillRule) {
        svgPathPeer.updateFillRule(fillRule);
    }

    @Override
    public void updateContent(String content) {
        svgPathPeer.updatePath(content);
    }
}
