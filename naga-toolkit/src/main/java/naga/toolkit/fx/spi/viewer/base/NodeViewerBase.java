package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;
import naga.toolkit.fx.scene.*;
import naga.toolkit.fx.scene.transform.*;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.util.ObservableLists;
import naga.toolkit.util.Properties;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class NodeViewerBase
        <N extends Node, NB extends NodeViewerBase<N, NB, NM>, NM extends NodeViewerMixin<N, NB, NM>>

        implements NodeViewer<N> {

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
                , node.clipProperty()
                , node.blendModeProperty()
                , node.effectProperty()
                , node.layoutXProperty()
                , node.layoutYProperty()
                , node.onMouseClickedProperty()
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

    void requestUpdateOnPropertiesChange(SceneRequester sceneRequester, ObservableValue... properties) {
        Properties.runOnPropertiesChange(property -> requestUpdateProperty(sceneRequester, property), properties);
    }

    private void requestUpdateProperty(SceneRequester sceneRequester, ObservableValue changedProperty) {
        sceneRequester.requestNodeViewerPropertyUpdate(node, changedProperty);
    }

    void requestUpdateOnListsChange(SceneRequester sceneRequester, ObservableList... lists) {
        Arrays.forEach(lists, list -> requestUpdateOnListChange(sceneRequester, list));
    }

    void requestUpdateOnListChange(SceneRequester sceneRequester, ObservableList list) {
        ObservableLists.runOnListChange(() -> requestUpdateList(sceneRequester, list), list);
    }

    void requestUpdateList(SceneRequester sceneRequester, ObservableList changedList) {
        sceneRequester.requestNodeViewerListUpdate(node, changedList);
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return updateProperty(node.onMouseClickedProperty(), changedProperty, mixin::updateOnMouseClicked)
                || updateProperty(node.mouseTransparentProperty(), changedProperty, mixin::updateMouseTransparent)
                || updateProperty(node.visibleProperty(), changedProperty, mixin::updateVisible)
                || updateProperty(node.opacityProperty(), changedProperty, mixin::updateOpacity)
                || updateProperty(node.clipProperty(), changedProperty, mixin::updateClip)
                || updateProperty(node.blendModeProperty(), changedProperty, mixin::updateBlendMode)
                || updateProperty(node.effectProperty(), changedProperty, mixin::updateEffect)
                || updateProperty(node.layoutXProperty(), changedProperty, mixin::updateLayoutX)
                || updateProperty(node.layoutYProperty(), changedProperty, mixin::updateLayoutY)
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
                Scene scene = node.getScene();
                if (scene instanceof CanvasScene)
                    ((CanvasScene) scene).requestCanvasRepaint();
            }, properties);
    }


    <T> boolean updateProperty(Property<T> property, ObservableValue changedProperty, Consumer<T> updater) {
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
