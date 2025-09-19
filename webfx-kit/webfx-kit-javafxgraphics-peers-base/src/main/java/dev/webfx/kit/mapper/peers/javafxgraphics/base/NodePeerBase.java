package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.util.properties.ObservableLists;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.util.Arrays;
import dev.webfx.platform.util.collection.Collections;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class NodePeerBase
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
            , node.scaleXProperty()
            , node.scaleYProperty()
            , node.layoutXProperty()
            , node.layoutYProperty()
            , node.translateXProperty()
            , node.translateYProperty()
            , node.rotateProperty()
            , node.cursorProperty()
            , node.mouseTransparentProperty()
            , node.onMouseDraggedProperty()
            , node.styleProperty()
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
        FXProperties.runOnPropertiesChange(property -> requestUpdateProperty(sceneRequester, property), properties);
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
               || updateProperty(node.opacityProperty(), changedProperty, p -> mixin.updateOpacity(p.doubleValue()))
               || updateProperty(node.clipProperty(), changedProperty, mixin::updateClip)
               || updateProperty(node.blendModeProperty(), changedProperty, mixin::updateBlendMode)
               || updateProperty(node.effectProperty(), changedProperty, mixin::updateEffect)
               || updateProperty(node.layoutXProperty(), changedProperty, p -> mixin.updateLayoutX(p.doubleValue()))
               || updateProperty(node.layoutYProperty(), changedProperty, p -> mixin.updateLayoutY(p.doubleValue()))
               || updateProperty(node.translateXProperty(), changedProperty, p -> mixin.updateTranslateX(p.doubleValue()))
               || updateProperty(node.translateYProperty(), changedProperty, p -> mixin.updateTranslateY(p.doubleValue()))
               || updateProperty(node.rotateProperty(), changedProperty, p -> mixin.updateTranslateY(p.doubleValue()))
               || updateProperty(node.scaleXProperty(), changedProperty, p -> mixin.updateScaleX(p.doubleValue()))
               || updateProperty(node.scaleYProperty(), changedProperty, p -> mixin.updateScaleY(p.doubleValue()))
               || updateProperty(node.cursorProperty(), changedProperty, mixin::updateCursor)
               || updateProperty(node.onDragDetectedProperty(), changedProperty, mixin::updateOnDragDetected)
               || updateProperty(node.onDragEnteredProperty(), changedProperty, mixin::updateOnDragEntered)
               || updateProperty(node.onDragOverProperty(), changedProperty, mixin::updateOnDragOver)
               || updateProperty(node.onDragDroppedProperty(), changedProperty, mixin::updateOnDragDropped)
               || updateProperty(node.onDragExitedProperty(), changedProperty, mixin::updateOnDragExited)
               || updateProperty(node.onDragDoneProperty(), changedProperty, mixin::updateOnDragDone)
               || updateProperty(node.styleProperty(), changedProperty, mixin::updateStyle)
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
            FXProperties.runOnPropertiesChange(() -> {
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

    protected <T> boolean updateList2(ObservableList<T> list, ObservableList<T> changedList, Consumer<List<T>> updater) {
        boolean hitChangedProperty = list == changedList;
        if (hitChangedProperty || changedList == null)
            updater.accept(list);
        return hitChangedProperty;
    }
}
