package naga.fx.spi.jfoenix;

import naga.fx.spi.javafx.fx.FxNodeViewerFactory;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
class JFXNodeViewerFactory extends FxNodeViewerFactory {

    final static JFXNodeViewerFactory SINGLETON = new JFXNodeViewerFactory();

    private JFXNodeViewerFactory() {
        registerNodeViewerFactory(Button.class, JFXButtonViewer::new);
        registerNodeViewerFactory(CheckBox.class, JFXCheckboxViewer::new);
    }
}
