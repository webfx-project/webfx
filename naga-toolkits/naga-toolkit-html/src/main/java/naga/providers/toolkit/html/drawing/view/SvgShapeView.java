package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import javafx.beans.property.Property;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.view.base.ShapeViewBase;
import naga.toolkit.drawing.spi.view.mixin.ShapeViewMixin;

/**
 * @author Bruno Salmon
 */
abstract class SvgShapeView<S extends Shape, SV extends ShapeViewBase<S>> extends SvgDrawableView<S> implements ShapeViewMixin<S, SV> {

    SvgShapeView(Element svgElement) {
        super(svgElement);
    }

    @Override
    public boolean update(Property changedProperty) {
        S shape = getDrawableViewBase().getDrawable();
        setSvgAttribute("stroke-dasharray", Collections.toStringWithNoBrackets(shape.getStrokeDashArray()));
        return updateSvgPaintAttribute("fill", shape.fillProperty(), changedProperty)
                || updateSvgPaintAttribute("stroke", shape.strokeProperty(), changedProperty)
                || updateSvgStringAttribute("shape-rendering", shape.smoothProperty(), smooth -> smooth ? "geometricPrecision" : "crispEdges", changedProperty)
                || updateSvgDoubleAttribute("stroke-width", shape.strokeWidthProperty(), changedProperty)
                || updateSvgStringAttribute("stroke-linecap", shape.strokeLineCapProperty(), SvgUtil::toSvgStrokeLineCap, changedProperty)
                || updateSvgStringAttribute("stroke-linejoin", shape.strokeLineJoinProperty(), SvgUtil::toSvgStrokeLineJoin, changedProperty)
                || updateSvgDoubleAttribute("stroke-miterlimit", shape.strokeMiterLimitProperty(), changedProperty)
                || updateSvgDoubleAttribute("stroke-dashoffset", shape.strokeDashOffsetProperty(), changedProperty);
    }
}
