package naga.fx.spi.javafx.skin;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.FxNodePeerFactory;
import naga.fx.spi.javafx.peer.FxNodePeer;
import naga.fx.spi.peer.NodePeer;

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
                    Toolkit.get().scheduler().runInUiThread(() ->
                            nodePeer.updateProperty(changedProperty)
                    );
                }

                @Override
                public void requestNodePeerListUpdate(Node node, ObservableList changedList, ListChangeListener.Change change) {
                    Toolkit.get().scheduler().runInUiThread(() ->
                            nodePeer.updateList(changedList, change)
                    );
                }
            });
            if (nodePeer instanceof FxNodePeer)
                getChildren().setAll(((FxNodePeer) nodePeer).getFxNode());
        }
    }
}
