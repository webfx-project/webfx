package naga.fx.spi.jfoenix;

import naga.fx.spi.javafx.FxNodePeerFactory;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
class JFXNodePeerFactory extends FxNodePeerFactory {

    final static JFXNodePeerFactory SINGLETON = new JFXNodePeerFactory();

    private JFXNodePeerFactory() {
        registerNodePeerFactory(Button.class, JFXButtonPeer::new);
        registerNodePeerFactory(CheckBox.class, JFXCheckboxPeer::new);
    }
}
