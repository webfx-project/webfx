package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.impl.canvas.CanvasDrawingImpl;
import naga.toolkit.fx.spi.view.NodeView;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Scale;
import naga.toolkit.transform.Transform;
import naga.toolkit.transform.Translate;
import naga.toolkit.util.ObservableLists;
import naga.toolkit.util.Properties;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class NodeViewBase
        <N extends Node, NV extends NodeViewBase<N, NV, NM>, NM extends NodeViewMixin<N, NV, NM>>
        implements NodeView<N> {

    protected N node;
    protected NM mixin;

    public void setMixin(NM mixin) {
        this.mixin = mixin;
    }

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        this.node = node;
        requestUpdateProperty(drawingRequester, null);
        requestUpdateList(drawingRequester, null);
        requestUpdateOnListChange(drawingRequester, node.getTransforms());
        requestUpdateOnPropertiesChange(drawingRequester,
                node.visibleProperty(),
                node.opacityProperty(),
                node.clipProperty(),
                node.blendModeProperty(),
                node.effectProperty(),
                node.layoutXProperty(),
                node.layoutYProperty(),
                node.onMouseClickedProperty(),
                node.mouseTransparentProperty());
    }

    @Override
    public void unbind() {
        node = null;
    }

    public N getNode() {
        return node;
    }

    void requestUpdateOnPropertiesChange(DrawingRequester drawingRequester, Property... properties) {
        Properties.runOnPropertiesChange(property -> requestUpdateProperty(drawingRequester, property), properties);
    }

    private void requestUpdateProperty(DrawingRequester drawingRequester, Property changedProperty) {
        drawingRequester.requestViewPropertyUpdate(node, changedProperty);
    }

    void requestUpdateOnListsChange(DrawingRequester drawingRequester, ObservableList... lists) {
        Arrays.forEach(lists, list -> requestUpdateOnListChange(drawingRequester, list));
    }

    void requestUpdateOnListChange(DrawingRequester drawingRequester, ObservableList list) {
        ObservableLists.runOnListChange(() -> requestUpdateList(drawingRequester, list), list);
    }

    void requestUpdateList(DrawingRequester drawingRequester, ObservableList changedList) {
        drawingRequester.requestViewListUpdate(node, changedList);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return updateProperty(node.onMouseClickedProperty(), changedProperty, mixin::updateOnMouseClicked)
                || updateProperty(node.mouseTransparentProperty(), changedProperty, mixin::updateMouseTransparent)
                || updateProperty(node.visibleProperty(), changedProperty, mixin::updateVisible)
                || updateProperty(node.opacityProperty(), changedProperty, mixin::updateOpacity)
                || updateProperty(node.clipProperty(), changedProperty, mixin::updateClip)
                || updateProperty(node.blendModeProperty(), changedProperty, mixin::updateBlendMode)
                || updateProperty(node.effectProperty(), changedProperty, mixin::updateEffect)
                || updateProperty(node.layoutXProperty(), changedProperty, mixin::updateLayoutX)
                || updateProperty(node.layoutYProperty(), changedProperty, mixin::updateLayoutY);
    }

    @Override
    public boolean updateList(ObservableList changedList) {
        return updateList(node.getTransforms(), changedList, this::updateTransforms);
    }

    private void updateTransforms(List<Transform> transforms) {
        mixin.updateTransforms(transforms);
        Collections.forEach(transforms, this::bindTransform);
    }

    private void bindTransform(Transform transform) {
        DrawingImpl drawing = DrawingImpl.getThreadLocalDrawing();
        Property[] properties = null;
        if (transform instanceof Translate) {
            Translate translate = (Translate) transform;
            properties = new Property[]{translate.xProperty(), translate.yProperty()};
        } else if (transform instanceof Rotate) {
            Rotate rotate = (Rotate) transform;
            properties = new Property[]{rotate.angleProperty(), rotate.pivotXProperty(), rotate.pivotYProperty()};
        } else if (transform instanceof Scale) {
            Scale scale = (Scale) transform;
            properties = new Property[]{scale.xProperty(), scale.yProperty()};
        }
        if (properties != null)
            Properties.runOnPropertiesChange(arg -> {
                mixin.updateTransforms(node.getTransforms());
                if (drawing instanceof CanvasDrawingImpl)
                    ((CanvasDrawingImpl) drawing).requestCanvasRepaint();
            }, properties);
    }


    <T> boolean updateProperty(Property<T> property, Property changedProperty, Consumer<T> updater) {
        boolean hitChangedProperty = property == changedProperty;
        if (hitChangedProperty || changedProperty == null)
            updater.accept(property.getValue());
        return hitChangedProperty;
    }

    <T> boolean updateList(ObservableList<T> list, ObservableList changedList, Consumer<List<T>> updater) {
        boolean hitChangedProperty = list == changedList;
        if (hitChangedProperty || changedList == null)
            updater.accept(list);
        return hitChangedProperty;
    }
}
