package naga.providers.toolkit.html.drawing.html.view;

import elemental2.HTMLElement;
import naga.toolkit.drawing.geometry.BoundingBox;
import naga.toolkit.drawing.geometry.Bounds;
import naga.toolkit.drawing.scene.LayoutMeasurable;

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
        return prefWidth(height);
    }

    default double maxWidth(double height) {
        return prefWidth(height);
    }

    default double minHeight(double width) {
        return prefHeight(width);
    }

    default double maxHeight(double width) {
        return prefHeight(width);
    }

    default double prefWidth(double height) {
        return getElement().offsetWidth;
    }

    default double prefHeight(double width) {
        return getElement().offsetHeight;
    }

}
