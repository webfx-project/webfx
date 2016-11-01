package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.Node;
import naga.toolkit.drawing.shapes.Drawable;

/**
 * @author Bruno Salmon
 */
abstract class FxDrawableViewImpl<D extends Drawable, N extends Node> implements FxDrawableView<D, N> {

    N fxDrawableNode;

    void setAndBindDrawableProperties(D drawable, N fxShape) {
        this.fxDrawableNode = fxShape;
    }

    @Override
    public void unbind() {
        fxDrawableNode = null;
    }

    public N getFxDrawableNode() {
        return fxDrawableNode;
    }

}
