package naga.providers.toolkit.javafx.drawing.view;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import naga.providers.toolkit.javafx.util.FxTransforms;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
abstract class FxDrawableViewImpl<D extends Drawable, N extends Node> implements FxDrawableView<D, N> {

    N fxDrawableNode;

    void setAndBindDrawableProperties(D drawable, N fxDrawableNode) {
        this.fxDrawableNode = fxDrawableNode;
        ObservableLists.bindConverted(fxDrawableNode.getTransforms(), drawable.getTransforms(), FxTransforms::toFxTransform);
    }

    @Override
    public boolean updateList(ObservableList changedList) {
        return false;
    }

    @Override
    public void unbind() {
        fxDrawableNode = null;
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return false;
    }

    public N getFxDrawableNode() {
        return fxDrawableNode;
    }

}
