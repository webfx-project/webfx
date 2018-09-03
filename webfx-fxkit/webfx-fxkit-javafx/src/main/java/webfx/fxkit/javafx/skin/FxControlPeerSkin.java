package webfx.fxkit.javafx.skin;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import webfx.fxkits.core.scene.SceneRequester;
import webfx.fxkits.core.spi.FxKit;
import webfx.fxkit.javafx.FxNodePeerFactory;
import webfx.fxkit.javafx.peer.FxNodePeer;
import webfx.fxkits.core.spi.peer.NodePeer;

/**
 * @author Bruno Salmon
 */
public class FxControlPeerSkin<C extends Control> extends SkinBase<C> {

    public FxControlPeerSkin(C control) {
        super(control);
        NodePeer<C> nodePeer = FxNodePeerFactory.SINGLETON.createNodePeer(control);
        if (nodePeer != null) {
            nodePeer.bind(control, new SceneRequester() {
                @Override
                public void requestNodePeerPropertyUpdate(Node node, ObservableValue changedProperty) {
                    FxKit.get().scheduler().runInUiThread(() ->
                            nodePeer.updateProperty(changedProperty)
                    );
                }

                @Override
                public void requestNodePeerListUpdate(Node node, ObservableList changedList, ListChangeListener.Change change) {
                    FxKit.get().scheduler().runInUiThread(() ->
                            nodePeer.updateList(changedList, change)
                    );
                }
            });
            if (nodePeer instanceof FxNodePeer)
                getChildren().setAll(((FxNodePeer) nodePeer).getFxNode());
        }
    }
}
