package naga.providers.toolkit.jfoenix;

import naga.providers.toolkit.javafx.fx.FxNodeViewerFactory;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.CheckBox;

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
