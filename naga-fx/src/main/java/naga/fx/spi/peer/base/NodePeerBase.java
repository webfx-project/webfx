package naga.fx.spi.peer.base;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;
import naga.fx.properties.ObservableLists;
import naga.fx.properties.Properties;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.peer.NodePeer;

import java.util.List;

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
        requestUpdateList(sceneRequester, null);
        requestUpdateOnListChange(sceneRequester, node.getTransforms());
        requestUpdateOnPropertiesChange(sceneRequester
                , node.visibleProperty()
                , node.opacityProperty()
                , node.disabledProperty()
                , node.clipProperty()
                , node.blendModeProperty()
                , node.effectProperty()
                , node.layoutXProperty()
                , node.layoutYProperty()
                , node.mouseTransparentProperty()
        );
    }

    @Override
    public void unbind() {
        node = null;
    }

    public N getNode() {
        return node;
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

    void requestUpdateOnListChange(SceneRequester sceneRequester, ObservableList list) {
        ObservableLists.runOnListChange(() -> requestUpdateList(sceneRequester, list), list);
    }

    void requestUpdateList(SceneRequester sceneRequester, ObservableList changedList) {
        sceneRequester.requestNodePeerListUpdate(node, changedList);
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return updateProperty(node.mouseTransparentProperty(), changedProperty, mixin::updateMouseTransparent)
                || updateProperty(node.visibleProperty(), changedProperty, mixin::updateVisible)
                || updateProperty(node.disabledProperty(), changedProperty, mixin::updateDisabled)
                || updateProperty(node.opacityProperty(), changedProperty, p-> mixin.updateOpacity(p.doubleValue()))
                || updateProperty(node.clipProperty(), changedProperty, mixin::updateClip)
                || updateProperty(node.blendModeProperty(), changedProperty, mixin::updateBlendMode)
                || updateProperty(node.effectProperty(), changedProperty, mixin::updateEffect)
                || updateProperty(node.layoutXProperty(), changedProperty, p -> mixin.updateLayoutX(p.doubleValue()))
                || updateProperty(node.layoutYProperty(), changedProperty, p-> mixin.updateLayoutY(p.doubleValue()))
                ;
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

    <T> boolean updateList(ObservableList<T> list, ObservableList changedList, Consumer<List<T>> updater) {
        boolean hitChangedProperty = list == changedList;
        if (hitChangedProperty || changedList == null)
            updater.accept(list);
        return hitChangedProperty;
    }
}
