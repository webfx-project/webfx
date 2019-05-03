package webfx.fxkit.gwt.mapper.html.peer.javafxgraphics;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.collections.ListChangeListener;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import webfx.fxkit.gwt.mapper.svg.peer.javafxgraphics.SvgPathPeer;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import webfx.fxkit.mapper.spi.SceneRequester;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.PathPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.PathPeerMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class HtmlPathPeer
        <N extends Path, NB extends PathPeerBase<N, NB, NM>, NM extends PathPeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements PathPeerMixin<N, NB, NM> {

    private SvgPathPeer svgPathPeer = new SvgPathPeer();

    public HtmlPathPeer() {
        this((NB) new PathPeerBase(), HtmlUtil.createElement("div"));
    }

    public HtmlPathPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        getNodePeerBase().setNode(node);
        svgPathPeer.bind(node, sceneRequester);
        Element svgElement = SvgUtil.createSvgElement("svg");
        // Setting arbitrary large size to avoid the path to be cropped by the svg tag
        svgElement.setAttribute("width", 100_000);
        svgElement.setAttribute("height", 100_000);
        HtmlUtil.setChild(svgElement, svgPathPeer.getElement());
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
