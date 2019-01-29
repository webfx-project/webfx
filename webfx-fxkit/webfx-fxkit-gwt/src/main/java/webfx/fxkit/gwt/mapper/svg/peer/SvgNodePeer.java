package webfx.fxkit.gwt.mapper.svg.peer;

import elemental2.dom.Element;
import webfx.fxkit.gwt.mapper.svg.SvgScenePeer;
import webfx.fxkit.gwt.mapper.shared.HtmlSvgNodePeer;
import webfx.fxkit.gwt.mapper.util.HtmlPaints;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import webfx.fxkit.mapper.spi.NodePeer;
import webfx.fxkit.mapper.spi.impl.peer.NodePeerBase;
import webfx.fxkit.mapper.spi.impl.peer.NodePeerMixin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class SvgNodePeer
        <N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        extends HtmlSvgNodePeer<Element, N, NB, NM> {

    private Map<String, Element> svgLinearGradients;
    private Element svgClipPath;

    SvgNodePeer(NB base, Element element) {
        super(base, element);
    }

    @Override
    protected String toClipPath(Node clip) {
        String value = null;
        if (clip != null) {
            NodePeer nodePeer = clip.getOrCreateAndBindNodePeer();
            if (svgClipPath == null)
                svgClipPath = getSvgScene().addDef(SvgUtil.createClipPath());
            HtmlUtil.setChild(svgClipPath, ((SvgNodePeer) nodePeer).getElement());
            value = SvgUtil.getDefUrl(svgClipPath);
        }
        return value;
    }

    private SvgScenePeer getSvgScene() {
        return (SvgScenePeer) getNode().getScene().impl_getPeer();
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
