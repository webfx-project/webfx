package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.providers.toolkit.html.fx.shared.HtmlSvgNodeViewer;
import naga.providers.toolkit.html.fx.svg.SvgScene;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.effect.Effect;
import naga.toolkit.fx.scene.effect.GaussianBlur;
import naga.toolkit.fx.scene.paint.Color;
import naga.toolkit.fx.scene.paint.LinearGradient;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.base.NodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.NodeViewerMixin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class SvgNodeViewer
        <N extends Node, NV extends NodeViewerBase<N, NV, NM>, NM extends NodeViewerMixin<N, NV, NM>>

        extends HtmlSvgNodeViewer<Element, N, NV, NM> {

    private Map<String, Element> svgLinearGradients;
    private Element svgClipPath;

    SvgNodeViewer(NV base, Element element) {
        super(base, element);
    }

    @Override
    protected String toClipPath(Node clip) {
        String value = null;
        if (clip != null) {
            NodeViewer nodeViewer = clip.getOrCreateAndBindNodeViewer();
            if (svgClipPath == null)
                svgClipPath = getSvgScene().addDef(SvgUtil.createClipPath());
            HtmlUtil.setChild(svgClipPath, ((SvgNodeViewer) nodeViewer).getElement());
            value = SvgUtil.getDefUrl(svgClipPath);
        }
        return value;
    }

    private SvgScene getSvgScene() {
        return ((SvgScene) getNode().getScene());
    }

    @Override
    protected String toFilter(Effect effect) {
        return SvgUtil.getDefUrl(toSvgEffectFilter(effect));
    }

    private Element toSvgEffectFilter(Effect effect) {
        Element filterPrimitive = toSvgEffectFilterPrimitive(effect);
        if (filterPrimitive == null)
            return null;
        return getSvgScene().addDef(HtmlUtil.appendChild(SvgUtil.createFilter(), filterPrimitive));
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
                svgLinearGradients.put(name, svgLinearGradient = getSvgScene().addDef(SvgUtil.createLinearGradient()));
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
