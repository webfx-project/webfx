package webfx.fxkit.gwt.mapper.svg.peer.javafxgraphics;

import elemental2.dom.Element;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import webfx.fxkit.gwt.mapper.shared.HtmlSvgNodePeer;
import webfx.fxkit.gwt.mapper.shared.SvgRoot;
import webfx.fxkit.gwt.mapper.util.HtmlPaints;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.ScenePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.NodePeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.NodePeerMixin;

import java.util.*;

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
                svgClipPath = getSvgRoot().addDef(SvgUtil.createClipPath());
            HtmlUtil.setChild(svgClipPath, ((SvgNodePeer) nodePeer).getElement());
            value = SvgUtil.getDefUrl(svgClipPath);
        }
        return value;
    }

    private SvgRoot getSvgRoot() {
        ScenePeer scenePeer = getNode().getScene().impl_getPeer();
        if (scenePeer instanceof SvgRoot)
            return (SvgRoot) scenePeer;
        return (SvgRoot) getNode().getProperties().get("svgRoot");
    }

    @Override
    protected String toFilter(Effect effect) {
        return SvgUtil.getDefUrl(toSvgEffectFilter(effect));
    }

    private Element toSvgEffectFilter(Effect effect) {
        Collection<Element> filterPrimitives = toSvgEffectFilterPrimitives(effect);
        if (filterPrimitives == null || filterPrimitives.isEmpty())
            return null;
        Element filter = SvgUtil.createFilter();
        filterPrimitives.forEach(filter::appendChild);
        return getSvgRoot().addDef(filter);
    }

    private static Collection<Element> toSvgEffectFilterPrimitives(Effect effect) {
        if (effect == null)
            return null;
        if (effect instanceof GaussianBlur) {
            Element fe = SvgUtil.createSvgElement("feGaussianBlur");
            fe.setAttribute("in", "SourceGraphic");
            fe.setAttribute("stdDeviation", ((GaussianBlur) effect).getSigma());
            return Collections.singleton(fe);
        }
        if (effect instanceof BoxBlur) {
            // Is it supported by SVG? For now doing a gaussian blur instead
            Element fe = SvgUtil.createSvgElement("feGaussianBlur");
            fe.setAttribute("in", "SourceGraphic");
            fe.setAttribute("stdDeviation", GaussianBlur.getSigma(((BoxBlur) effect).getWidth()));
            return Collections.singleton(fe);
        }
        if (effect instanceof DropShadow) {
            DropShadow dropShadow = (DropShadow) effect;
            Element fe = SvgUtil.createSvgElement("feDropShadow");
            fe.setAttribute("dx", dropShadow.getOffsetX());
            fe.setAttribute("dy", dropShadow.getOffsetY());
            fe.setAttribute("stdDeviation", dropShadow.getRadius() / 2);
            fe.setAttribute("flood-color", HtmlPaints.toSvgCssPaint(dropShadow.getColor()));
            return Collections.singleton(fe);
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
                svgLinearGradients.put(name, svgLinearGradient = getSvgRoot().addDef(SvgUtil.createLinearGradient()));
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
