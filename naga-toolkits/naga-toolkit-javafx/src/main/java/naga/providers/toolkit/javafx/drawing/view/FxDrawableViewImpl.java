package naga.providers.toolkit.javafx.drawing.view;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import naga.providers.toolkit.javafx.events.FxMouseEvent;
import naga.providers.toolkit.javafx.util.FxTransforms;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.properties.conversion.ConvertedProperty;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
abstract class FxDrawableViewImpl<D extends Drawable, N extends Node> implements FxDrawableView<D, N> {

    N fxDrawableNode;

    void setAndBindDrawableProperties(D drawable, N fxDrawableNode) {
        this.fxDrawableNode = fxDrawableNode;
        ObservableLists.bindConverted(fxDrawableNode.getTransforms(), drawable.getTransforms(), FxTransforms::toFxTransform);
        fxDrawableNode.onMouseClickedProperty().bind(new ConvertedProperty<>(drawable.onMouseClickedProperty(), FxDrawableViewImpl::toFxMouseEventHandler));
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

    private static EventHandler<? super javafx.scene.input.MouseEvent> toFxMouseEventHandler(UiEventHandler<? super MouseEvent> mouseEventHandler) {
        return mouseEventHandler == null ? null : (EventHandler<javafx.scene.input.MouseEvent>) event -> mouseEventHandler.handle(new FxMouseEvent(event));
    }

}
