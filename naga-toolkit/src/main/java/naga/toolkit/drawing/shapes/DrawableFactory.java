package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.*;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
public interface DrawableFactory {

    static DrawableFactory get() {
        return DrawableFactoryImpl.SINGLETON;
    }

    default Rectangle createRectangle() {
        return new RectangleImpl();
    }

    default Rectangle createRectangle(double width, double height) {
        return new RectangleImpl(width, height);
    }

    default Rectangle createRectangle(double x, double y, double width, double height) {
        return new RectangleImpl(x, y, width, height);
    }

    default TextShape createText() {
        return new TextShapeImpl();
    }

    default Circle createCircle() {
        return new CircleImpl();
    }

    default Circle createCircle(double radius) {
        return new CircleImpl(radius);
    }

    default Circle createCircle(double centerX, double centerY, double radius) {
        return new CircleImpl(centerX, centerY, radius);
    }

    default Group createGroup() {
        return new GroupImpl();
    }

    default Group createGroup(Drawable... drawables) {
        Group group = createGroup();
        ObservableLists.setAllNonNulls(group.getDrawableChildren(), drawables);
        return group;
    }

    default EmbedDrawable createEmbed(GuiNode guiNode) {
        return new EmbedDrawableImpl(guiNode);
    }

}
