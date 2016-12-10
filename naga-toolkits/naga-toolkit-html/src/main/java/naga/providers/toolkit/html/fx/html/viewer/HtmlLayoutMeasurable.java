package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLElement;
import naga.toolkit.fx.geometry.BoundingBox;
import naga.toolkit.fx.geometry.Bounds;
import naga.toolkit.fx.scene.LayoutMeasurable;

/**
 * @author Bruno Salmon
 */
public interface HtmlLayoutMeasurable extends LayoutMeasurable {

    HTMLElement getElement();

    default Bounds getLayoutBounds() {
        HTMLElement e = getElement();
        return new BoundingBox(0, 0, 0, e.offsetWidth, e.offsetHeight, 0);
    }

    default double minWidth(double height) {
        return measureWidth(height);
    }

    default double maxWidth(double height) {
        return measureWidth(height);
    }

    default double minHeight(double width) {
        return measureHeight(width);
    }

    default double maxHeight(double width) {
        return measureHeight(width);
    }

    default double prefWidth(double height) {
        return measureWidth(height);
    }

    default double prefHeight(double width) {
        return measureHeight(width);
    }

    default double measureWidth(double height) {
        return measure(height, true);
    }

    default double measureHeight(double width) {
        return measure(width, false);
    }

    default double measure(double value, boolean width) {
        HTMLElement e = getElement();
        CSSStyleDeclaration style = e.style;
        Object styleWidth = style.width;
        Object styleHeight = style.height;
        style.width =   width && value >= 0 ? value : null;
        style.height = !width && value >= 0 ? value : null;
        double result = width ? e.offsetWidth : e.offsetHeight;
        style.width = styleWidth;
        style.height = styleHeight;
        return result;
    }
}
