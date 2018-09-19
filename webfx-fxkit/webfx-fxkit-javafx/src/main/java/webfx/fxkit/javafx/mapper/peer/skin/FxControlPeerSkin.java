package webfx.fxkit.javafx.mapper.peer.skin;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import webfx.fxkit.javafx.mapper.peer.FxNodePeer;
import webfx.fxkits.core.mapper.FxKitMapper;
import webfx.fxkits.core.mapper.spi.NodePeer;
import webfx.fxkits.core.mapper.spi.SceneRequester;
import webfx.platforms.core.services.uischeduler.UiScheduler;

/**
 * @author Bruno Salmon
 */
public class FxControlPeerSkin<C extends Control> extends SkinBase<C> {

    public FxControlPeerSkin(C control) {
        super(control);
        NodePeer<C> nodePeer = FxKitMapper.createNodePeer(control);
        if (nodePeer != null) {
            nodePeer.bind(control, new SceneRequester() {
                @Override
                public void requestNodePeerPropertyUpdate(Node node, ObservableValue changedProperty) {
                    UiScheduler.runInUiThread(() ->
                            nodePeer.updateProperty(changedProperty)
                    );
                }

                @Override
                public void requestNodePeerListUpdate(Node node, ObservableList changedList, ListChangeListener.Change change) {
                    UiScheduler.runInUiThread(() ->
                            nodePeer.updateList(changedList, change)
                    );
                }
            });
            if (nodePeer instanceof FxNodePeer)
                getChildren().setAll(((FxNodePeer) nodePeer).getFxNode());
        }
    }
}
