package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg;

import elemental2.dom.Element;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.text.TextAlignment;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public abstract class SvgNodePeer
        <N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        extends HtmlSvgNodePeer<Element, N, NB, NM> {

    private Map<String, Element> svgLinearGradients, svgRadialGradients;
    private Element svgClipPath;

    SvgNodePeer(NB base, Element element) {
        super(base, element);
    }

    @Override
    protected String computeClipPath() {
        if (svgClipPath == null)
            svgClipPath = getSvgRoot().addDef(SvgUtil.createClipPath());
        HtmlUtil.setChild(svgClipPath, getElement());
        return SvgUtil.getDefUrl(svgClipPath);
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
        Collection<Element> filterPrimitives = toSvgEffectFilterPrimitives(effect, null, null);
        if (filterPrimitives == null || filterPrimitives.isEmpty())
            return null;
        Element filter = SvgUtil.createFilter();
        filterPrimitives.forEach(filter::appendChild);
        return getSvgRoot().addDef(filter);
    }

    private static Collection<Element> toSvgEffectFilterPrimitives(Effect effect, String filterResult, Collection<Element> filterPrimitives) {
        if (effect == null)
            return filterPrimitives;
        if (effect instanceof GaussianBlur) {
            Element fe = SvgUtil.createSvgElement("feGaussianBlur");
            fe.setAttribute("in", "SourceGraphic");
            fe.setAttribute("stdDeviation", ((GaussianBlur) effect).getSigma());
            if (filterResult != null)
                fe.setAttribute("result", filterResult);
            return addFilterPrimitive(filterPrimitives, fe);
        }
        if (effect instanceof BoxBlur) {
            // Is it supported by SVG? For now doing a gaussian blur instead
            Element fe = SvgUtil.createSvgElement("feGaussianBlur");
            fe.setAttribute("in", "SourceGraphic");
            fe.setAttribute("stdDeviation", GaussianBlur.getSigma(((BoxBlur) effect).getWidth()));
            if (filterResult != null)
                fe.setAttribute("result", filterResult);
            return addFilterPrimitive(filterPrimitives, fe);
        }
        if (effect instanceof DropShadow) {
            DropShadow dropShadow = (DropShadow) effect;
            Element fe = SvgUtil.createSvgElement("feDropShadow");
            fe.setAttribute("dx", dropShadow.getOffsetX());
            fe.setAttribute("dy", dropShadow.getOffsetY());
            fe.setAttribute("stdDeviation", dropShadow.getRadius() / 2);
            fe.setAttribute("flood-color", HtmlPaints.toSvgCssPaint(dropShadow.getColor()));
            Effect inputEffect = dropShadow.getInput();
            if (inputEffect != null) {
                String inputResult = SvgUtil.generateNewFilterId();
                filterPrimitives = toSvgEffectFilterPrimitives(inputEffect, inputResult, filterPrimitives);
                fe.setAttribute("in", inputResult);
            }
            if (filterResult != null)
                fe.setAttribute("result", filterResult);
            return addFilterPrimitive(filterPrimitives, fe);
        }
        if (effect instanceof InnerShadow) {
            InnerShadow innerShadow = (InnerShadow) effect;

            //String feBlurResult = SvgUtil.generateNewFilterId();
            Element feBlur = SvgUtil.createSvgElement("feGaussianBlur");
            feBlur.setAttribute("in", "SourceAlpha");
            feBlur.setAttribute("stdDeviation", innerShadow.getRadius() / 2);
            //feBlur.setAttribute("result", feBlurResult);

            Element feOffset = SvgUtil.createSvgElement("feOffset");
            feOffset.setAttribute("dx", innerShadow.getOffsetX());
            feOffset.setAttribute("dy", innerShadow.getOffsetY());

            Element feShadowDiff = SvgUtil.createSvgElement("feComposite");
            String feShadowDiffResult = SvgUtil.generateNewFilterId();
            feShadowDiff.setAttribute("in2", "SourceAlpha");
            feShadowDiff.setAttribute("operator", "arithmetic");
            feShadowDiff.setAttribute("k2", "-1");
            feShadowDiff.setAttribute("k3", "1");
            feShadowDiff.setAttribute("result", feShadowDiffResult);

            Element feFlood = SvgUtil.createSvgElement("feFlood");
            feFlood.setAttribute("flood-color", HtmlPaints.toSvgCssPaint(innerShadow.getColor()));

            Element feIn = SvgUtil.createSvgElement("feComposite");
            feIn.setAttribute("in2", feShadowDiffResult);
            feIn.setAttribute("operator", "in");

            Element feOver = SvgUtil.createSvgElement("feComposite");
            feOver.setAttribute("in2", "SourceGraphic");
            feOver.setAttribute("operator", "over");

            Effect inputEffect = innerShadow.getInput();
            if (inputEffect != null) {
                String inputResult = SvgUtil.generateNewFilterId();
                filterPrimitives = toSvgEffectFilterPrimitives(inputEffect, inputResult, filterPrimitives);
                feOver.setAttribute("in2", inputResult);
            }
            if (filterResult != null)
                feOver.setAttribute("result", filterResult);
            return addFilterPrimitive(filterPrimitives, feBlur, feOffset, feShadowDiff, feFlood, feIn, feOver);
        }
        return filterPrimitives;
    }

    private static Collection<Element> addFilterPrimitive(Collection<Element> allPrimitives, Element... primitivesToAdd) {
        if (primitivesToAdd.length > 0) {
            if (allPrimitives == null)
                allPrimitives = new ArrayList<>();
            Collections.addAll(allPrimitives, primitivesToAdd);
        }
        return allPrimitives;
    }

    void setPaintAttribute(String name, Paint paint) {
        setElementAttribute(name, toPaintAttribute(name, paint));
    }

    private String toPaintAttribute(String name, Paint paint) {
        String value = "none";
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
        } else if (paint instanceof RadialGradient) {
            if (svgRadialGradients == null)
                svgRadialGradients = new HashMap<>();
            Element svgRadialGradient = svgRadialGradients.get(name);
            if (svgRadialGradient == null)
                svgRadialGradients.put(name, svgRadialGradient = getSvgRoot().addDef(SvgUtil.createRadialGradient()));
            SvgUtil.updateRadialGradient((RadialGradient) paint, svgRadialGradient);
            value = SvgUtil.getDefUrl(svgRadialGradient);
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
