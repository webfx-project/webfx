package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.layoutmeasurable.HtmlLayoutCache;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoHGrow;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared.SvgRoot;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared.SvgRootBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.svg.SvgTextPeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.SvgUtil;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.TextPeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.TextPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlSvgTextPeer
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends HtmlShapePeer<N, NB, NM>
        implements TextPeerMixin<N, NB, NM>, HtmlLayoutMeasurableNoHGrow {

    private SvgTextPeer svgTextPeer = new SvgTextPeer();

    public HtmlSvgTextPeer() {
        this((NB) new TextPeerBase(), HtmlUtil.createElement("div"));
    }

    public HtmlSvgTextPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        getNodePeerBase().setNode(node);
        Element svgElement = SvgUtil.createSvgElement("svg");
        SvgRoot svgRoot = new SvgRootBase();
        node.getProperties().put("svgRoot", svgRoot);
        // Setting arbitrary large size to avoid the path to be cropped by the svg tag
        svgElement.setAttribute("width", 100_000);
        svgElement.setAttribute("height", 100_000);
        HtmlUtil.setChildren(svgElement, svgRoot.getDefsElement(), svgTextPeer.getElement());
        HtmlUtil.setChild(getElement(), svgElement);
        svgTextPeer.bind(node, sceneRequester);
    }

    @Override
    public NB getNodePeerBase() {
        return (NB) svgTextPeer.getNodePeerBase();
    }

    @Override
    public double measure(HTMLElement e, boolean width) {
        Element svgElement = svgTextPeer.getElement();
        return width ? svgElement.getBoundingClientRect().width : svgElement.getBoundingClientRect().height;
    }

    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }

    @Override
    public void updateText(String text) {}

    @Override
    public void updateTextOrigin(VPos textOrigin) {}

    @Override
    public void updateX(Double x) {}

    @Override
    public void updateY(Double y) {}

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {}

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {}

    @Override
    public void updateFont(Font font) {}

}
