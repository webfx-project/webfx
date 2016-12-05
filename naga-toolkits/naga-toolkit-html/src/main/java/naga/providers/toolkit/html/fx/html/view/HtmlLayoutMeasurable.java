package naga.providers.toolkit.html.fx.html.view;

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
        HTMLElement c = getElement();
        return new BoundingBox(0, 0, 0, c.offsetWidth, c.offsetHeight, 0);
    }

    default double minWidth(double height) {
        return offsetWidth(height);
    }

    default double maxWidth(double height) {
        return offsetWidth(height);
    }

    default double minHeight(double width) {
        return offsetHeight(width);
    }

    default double maxHeight(double width) {
        return offsetHeight(width);
    }

    default double prefWidth(double height) {
        return offsetWidth(height);
    }

    default double prefHeight(double width) {
        return offsetHeight(width);
    }

    default double offsetWidth(double height) {
        return measureOffset(height, true);
    }

    default double offsetHeight(double width) {
        return measureOffset(width, false);
    }

    default double measureOffset(double value, boolean width) {
        HTMLElement element = getElement();
        CSSStyleDeclaration style = element.style;
        Object styleWidth = style.width;
        Object styleHeight = style.height;
        style.width =   width && value >= 0 ? value : null;
        style.height = !width && value >= 0 ? value : null;
        double offset = width ? element.offsetWidth : element.offsetHeight;
        style.width = styleWidth;
        style.height = styleHeight;
        return offset;
    }
}
