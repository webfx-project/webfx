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

    default TextShape createText() {
        return new TextShapeImpl();
    }

    default Circle createCircle() {
        return new CircleImpl();
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
