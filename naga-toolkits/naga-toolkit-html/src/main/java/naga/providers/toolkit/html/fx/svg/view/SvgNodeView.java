package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.providers.toolkit.html.fx.shared.HtmlSvgNodeView;
import naga.providers.toolkit.html.fx.svg.SvgDrawing;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.effect.Effect;
import naga.toolkit.fx.effect.GaussianBlur;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.paint.Color;
import naga.toolkit.fx.paint.LinearGradient;
import naga.toolkit.fx.paint.Paint;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.view.NodeView;
import naga.toolkit.fx.spi.view.base.NodeViewBase;
import naga.toolkit.fx.spi.view.base.NodeViewMixin;
import naga.toolkit.fx.text.TextAlignment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class SvgNodeView
        <N extends Node, NV extends NodeViewBase<N, NV, NM>, NM extends NodeViewMixin<N, NV, NM>>
        extends HtmlSvgNodeView<Element, N, NV, NM> {

    private Map<String, Element> svgLinearGradients;
    private Element svgClipPath;

    SvgNodeView(NV base, Element element) {
        super(base, element);
    }

    @Override
    protected String toClipPath(Node clip) {
        String value = null;
        if (clip != null) {
            SvgDrawing drawing = (SvgDrawing) DrawingImpl.getThreadLocalDrawing();
            NodeView nodeView = drawing.getOrCreateAndBindNodeView(clip);
            if (svgClipPath == null)
                svgClipPath = drawing.addDef(SvgUtil.createClipPath());
            HtmlUtil.setChild(svgClipPath, ((SvgNodeView) nodeView).getElement());
            value = SvgUtil.getDefUrl(svgClipPath);
        }
        return value;
    }

    @Override
    protected String toFilter(Effect effect) {
        return SvgUtil.getDefUrl(toSvgEffectFilter(effect));
    }

    private static Element toSvgEffectFilter(Effect effect) {
        Element filterPrimitive = toSvgEffectFilterPrimitive(effect);
        if (filterPrimitive == null)
            return null;
        SvgDrawing drawing = (SvgDrawing) DrawingImpl.getThreadLocalDrawing();
        return drawing.addDef(HtmlUtil.appendChild(SvgUtil.createFilter(), filterPrimitive));
    }

    private static Element toSvgEffectFilterPrimitive(Effect effect) {
        if (effect == null)
            return null;
        if (effect instanceof GaussianBlur) {
            Element fe = SvgUtil.createSvgElement("feGaussianBlur");
            fe.setAttribute("in", "SourceGraphic");
            fe.setAttribute("stdDeviation", ((GaussianBlur) effect).getSigma());
            return fe;
        }
        return null;
    }

    void setPaintAttribute(String name, Paint paint) {
        setElementAttribute(name, toPaintAttribute(name, paint));
    }

    private String toPaintAttribute(String name, Paint paint) {
        String value = null;
        if (paint instanceof Color)
            value = HtmlPaints.toSvgCssPaint(paint);
        else if (paint instanceof LinearGradient) {
            if (svgLinearGradients == null)
                svgLinearGradients = new HashMap<>();
            Element svgLinearGradient = svgLinearGradients.get(name);
            if (svgLinearGradient == null)
                svgLinearGradients.put(name, svgLinearGradient = ((SvgDrawing) DrawingImpl.getThreadLocalDrawing()).addDef(SvgUtil.createLinearGradient()));
            SvgUtil.updateLinearGradient((LinearGradient) paint, svgLinearGradient);
            value = SvgUtil.getDefUrl(svgLinearGradient);
        }
        return value;
    }

    static String vPosToSvgAlignmentBaseLine(VPos vpos) {
        if (vpos != null)
            switch (vpos) {
                case TOP: return "text-before-edge";
                case CENTER: return "central";
                case BASELINE: return "baseline";
                case BOTTOM: return "text-after-edge";
            }
        return null;
    }

    static String textAlignmentToSvgTextAnchor(TextAlignment textAlignment) {
        if (textAlignment != null)
            switch (textAlignment) {
                case LEFT: return "start";
                case CENTER: return "middle";
                case RIGHT: return "end";
            }
        return null;
    }
}
