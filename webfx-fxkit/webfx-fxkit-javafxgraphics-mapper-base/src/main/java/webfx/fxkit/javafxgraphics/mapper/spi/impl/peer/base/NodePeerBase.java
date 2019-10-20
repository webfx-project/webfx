package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;
import webfx.fxkit.util.properties.ObservableLists;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.collection.Collections;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public abstract class NodePeerBase
        <N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        implements NodePeer<N> {

    protected N node;
    protected NM mixin;

    public void setMixin(NM mixin) {
        this.mixin = mixin;
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        this.node = node;
        requestUpdateProperty(sceneRequester, null);
        requestUpdateList(sceneRequester, null, null);
        requestUpdateOnListsChange(sceneRequester, node.getTransforms(), node.getStyleClass());
        requestUpdateOnPropertiesChange(sceneRequester
                , node.idProperty()
                , node.visibleProperty()
                , node.opacityProperty()
                , node.disabledProperty()
                , node.clipProperty()
                , node.blendModeProperty()
                , node.effectProperty()
                , node.layoutXProperty()
                , node.layoutYProperty()
                , node.mouseTransparentProperty()
                , node.onMouseDraggedProperty()
        );
    }

    @Override
    public void unbind() {
        node = null;
    }

    public N getNode() {
        return node;
    }

    public void setNode(N node) {
        this.node = node;
    }

    @Override
    public void requestFocus() {

    }

    protected void requestUpdateOnPropertiesChange(SceneRequester sceneRequester, ObservableValue... properties) {
        Properties.runOnPropertiesChange(property -> requestUpdateProperty(sceneRequester, property), properties);
    }

    private void requestUpdateProperty(SceneRequester sceneRequester, ObservableValue changedProperty) {
        sceneRequester.requestNodePeerPropertyUpdate(node, changedProperty);
    }

    void requestUpdateOnListsChange(SceneRequester sceneRequester, ObservableList... lists) {
        Arrays.forEach(lists, list -> requestUpdateOnListChange(sceneRequester, list));
    }

    protected void requestUpdateOnListChange(SceneRequester sceneRequester, ObservableList list) {
        ObservableLists.runOnListChange(c -> requestUpdateList(sceneRequester, list, c), list);
    }

    void requestUpdateList(SceneRequester sceneRequester, ObservableList list, ListChangeListener.Change change) {
        sceneRequester.requestNodePeerListUpdate(node, list, change);
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return updateProperty(node.mouseTransparentProperty(), changedProperty, mixin::updateMouseTransparent)
                || updateProperty(node.idProperty(), changedProperty, mixin::updateId)
                || updateProperty(node.visibleProperty(), changedProperty, mixin::updateVisible)
                || updateProperty(node.disabledProperty(), changedProperty, mixin::updateDisabled)
                || updateProperty(node.opacityProperty(), changedProperty, p-> mixin.updateOpacity(p.doubleValue()))
                || updateProperty(node.clipProperty(), changedProperty, mixin::updateClip)
                || updateProperty(node.blendModeProperty(), changedProperty, mixin::updateBlendMode)
                || updateProperty(node.effectProperty(), changedProperty, mixin::updateEffect)
                || updateProperty(node.layoutXProperty(), changedProperty, p -> mixin.updateLayoutX(p.doubleValue()))
                || updateProperty(node.layoutYProperty(), changedProperty, p -> mixin.updateLayoutY(p.doubleValue()))
                || updateProperty(node.cursorProperty(), changedProperty, mixin::updateCursor)
                || updateProperty(node.onDragDetectedProperty(), changedProperty, mixin::updateOnDragDetected)
                || updateProperty(node.onDragOverProperty(), changedProperty, mixin::updateOnDragOver)
                || updateProperty(node.onDragDroppedProperty(), changedProperty, mixin::updateOnDragDropped)
                ;
    }

    @Override
    public boolean updateList(ObservableList list, ListChangeListener.Change change) {
        return updateList2(node.getTransforms(), list, change, this::updateTransforms)
                || updateList2(node.getStyleClass(), list, change, mixin::updateStyleClass)
                ;
    }

    private void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change) {
        mixin.updateTransforms(transforms, change);
        Collections.forEach(transforms, this::bindTransform);
    }

    private void bindTransform(Transform transform) {
        Property[] properties = null;
        if (transform instanceof Translate) {
            Translate translate = (Translate) transform;
            properties = new Property[]{translate.xProperty(), translate.yProperty()};
        } else if (transform instanceof Rotate) {
            Rotate rotate = (Rotate) transform;
            properties = new Property[]{rotate.angleProperty(), rotate.pivotXProperty(), rotate.pivotYProperty()};
        } else if (transform instanceof Scale) {
            Scale scale = (Scale) transform;
            properties = new Property[]{scale.xProperty(), scale.yProperty(), scale.pivotXProperty(), scale.pivotYProperty()};
        }
        if (properties != null)
            Properties.runOnPropertiesChange(() -> {
                mixin.updateTransforms(node.getTransforms(), null);
/*
                ScenePeer scenePeer = node.getScene().impl_getPeer();
                if (scenePeer instanceof CanvasScenePeer)
                    ((CanvasScenePeer) scenePeer).requestCanvasRepaint();
*/
            }, properties);
    }


    protected <T> boolean updateProperty(ObservableValue<T> property, ObservableValue changedProperty, Consumer<T> updater) {
        boolean hitChangedProperty = property == changedProperty;
        if (hitChangedProperty || changedProperty == null)
            updater.accept(property.getValue());
        return hitChangedProperty;
    }

    protected <T> boolean updateList2(ObservableList<T> list, ObservableList<T> changedList, ListChangeListener.Change<T> change, BiConsumer<List<T>, ListChangeListener.Change<T>> updater) {
        boolean hitChangedProperty = list == changedList;
        if (hitChangedProperty || changedList == null)
            updater.accept(list, change);
        return hitChangedProperty;
    }
}
