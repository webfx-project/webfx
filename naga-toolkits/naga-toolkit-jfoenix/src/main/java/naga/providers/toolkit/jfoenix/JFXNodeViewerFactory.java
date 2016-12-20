package naga.providers.toolkit.jfoenix;

import naga.providers.toolkit.javafx.fx.FxNodeViewerFactory;
import naga.toolkit.fx.scene.control.impl.ButtonImpl;
import naga.toolkit.fx.scene.control.impl.CheckBoxImpl;

/**
 * @author Bruno Salmon
 */
class JFXNodeViewerFactory extends FxNodeViewerFactory {

    final static JFXNodeViewerFactory SINGLETON = new JFXNodeViewerFactory();

    private JFXNodeViewerFactory() {
        registerNodeViewerFactory(ButtonImpl.class, JFXButtonViewer::new);
        registerNodeViewerFactory(CheckBoxImpl.class, JFXCheckboxViewer::new);
    }
}
