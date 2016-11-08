package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.events.HtmlMouseEvent;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.providers.toolkit.html.util.SvgTransforms;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.base.DrawableViewBase;
import naga.toolkit.drawing.spi.view.base.DrawableViewImpl;
import naga.toolkit.drawing.spi.view.base.DrawableViewMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.transform.Transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public abstract class SvgDrawableView
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>
        extends DrawableViewImpl<D, DV, DM> {

    private final Element svgElement;
    private Map<String, Element> svgLinearGradients;

    SvgDrawableView(DV base, Element svgElement) {
        super(base);
        this.svgElement = svgElement;
    }

    public Element getElement() {
        return svgElement;
    }

    @Override
    public void updateTransforms(List<Transform> transforms) {
        setSvgAttribute("transform", SvgTransforms.toSvgTransforms(transforms));
    }

    @Override
    public void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked) {
        svgElement.onclick = onMouseClicked == null ? null : e -> {
            onMouseClicked.handle(new HtmlMouseEvent(e));
            return null;
        };
    }

    void setSvgAttribute(String name, String value) {
        setSvgAttribute(name, value, null);
    }

    void setSvgAttribute(String name, String value, String skipValue) {
        if (Objects.equals(value, skipValue))
            svgElement.removeAttribute(name);
        else
            svgElement.setAttribute(name, value);
    }

    void setSvgAttribute(String name, Double value) {
        setSvgAttribute(name, value, null);
    }

    void setSvgAttribute(String name, Double value, Double skipValue) {
        if (Objects.equals(value, skipValue))
            svgElement.removeAttribute(name);
        else
            svgElement.setAttribute(name, value);
    }

    void setSvgAttribute(String name, Integer value) {
        setSvgAttribute(name, value, null);
    }

    void setSvgAttribute(String name, Integer value, Integer skipValue) {
        if (Objects.equals(value, skipValue))
            svgElement.removeAttribute(name);
        else
            svgElement.setAttribute(name, value);
    }

    void setSvgFontAttributes(Font font) {
        setSvgAttribute("font-family", font.getFamily());
        setSvgAttribute("font-style", font.getPosture() == FontPosture.ITALIC ? "italic" : "normal", "normal");
        setSvgAttribute("font-weight", font.getWeight() == null ? 0 : font.getWeight().getWeight(), 0);
        setSvgAttribute("font-size", font.getSize());
    }

    void setSvgTextContent(String textContent) {
        svgElement.textContent = textContent;
    }

    void setPaintAttribute(String name, Paint paint) {
        setSvgAttribute(name, toPaintAttribute(name, paint));
    }

    private String toPaintAttribute(String name, Paint paint) {
        String value = null;
        if (paint instanceof Color)
            value = HtmlPaints.toCssPaint(paint);
        else if (paint instanceof LinearGradient) {
            if (svgLinearGradients == null)
                svgLinearGradients = new HashMap<>();
            Element svgLinearGradient = svgLinearGradients.get(name);
            if (svgLinearGradient == null)
                svgLinearGradients.put(name, svgLinearGradient = ((SvgDrawing) DrawingImpl.getThreadLocalDrawing()).addDef(SvgUtil.createLinearGradient()));
            SvgUtil.updateLinearGradient((LinearGradient) paint, svgLinearGradient);
            value = "url(#" + svgLinearGradient.getAttribute("id") + ")";
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
