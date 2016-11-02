package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import javafx.beans.property.Property;
import naga.commons.util.function.Converter;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.drawing.spi.view.DrawableView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public abstract class SvgDrawableView<S extends Drawable> implements DrawableView<S> {

    private final Element svgElement;
    private Map<String, Element> svgLinearGradients;

    SvgDrawableView(Element svgElement) {
        this.svgElement = svgElement;
    }

    public Element getElement() {
        return svgElement;
    }

    public abstract boolean update(SvgDrawing svgDrawing, Property changedProperty);

    <T> boolean updateSvgStringAttribute(String name, Property<T> property, Converter<T, String> converter, Property changedProperty) {
        boolean hitChangedProperty = property == changedProperty;
        if (hitChangedProperty || changedProperty == null)
            svgElement.setAttribute(name, converter.convert(property.getValue()));
        return hitChangedProperty;
    }

    boolean updateSvgDoubleAttribute(String name, Property<Double> property, Property changedProperty) {
        boolean hitChangedProperty = property == changedProperty;
        if (hitChangedProperty || changedProperty == null)
            svgElement.setAttribute(name, property.getValue());
        return hitChangedProperty;
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

    boolean updateSvgFontAttributes(Property<Font> property, Property changedProperty) {
        boolean hitProperty = property == changedProperty;
        if (hitProperty || changedProperty == null) {
            Font font = property.getValue();
            setSvgAttribute("font-family", font.getFamily());
            setSvgAttribute("font-style", font.getPosture() == FontPosture.ITALIC ? "italic" : "normal", "normal");
            setSvgAttribute("font-weight", font.getWeight() == null ? 0 : font.getWeight().getWeight(), 0);
            setSvgAttribute("font-size", font.getSize());
        }
        return hitProperty;
    }

    boolean updateSvgTextContent(Property<String> property, Property changedProperty) {
        boolean hitProperty = property == changedProperty;
        if (hitProperty || changedProperty == null)
            svgElement.textContent = property.getValue();
        return hitProperty;
    }

    boolean updateSvgPaintAttribute(String name, Property<Paint> property, Property changedProperty, SvgDrawing svgDrawing) {
        return updateSvgStringAttribute(name, property, paint -> toPaintAttribute(name, paint, svgDrawing), changedProperty);
    }

    private String toPaintAttribute(String name, Paint paint, SvgDrawing svgDrawing) {
        String value = null;
        if (paint instanceof Color)
            value = HtmlPaints.toCssPaint(paint);
        else if (paint instanceof LinearGradient) {
            if (svgLinearGradients == null)
                svgLinearGradients = new HashMap<>();
            Element svgLinearGradient = svgLinearGradients.get(name);
            if (svgLinearGradient == null)
                svgLinearGradients.put(name, svgLinearGradient = svgDrawing.addDef(SvgUtil.createLinearGradient()));
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
