package naga.providers.toolkit.jfoenix;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import naga.providers.toolkit.javafx.fx.FxNodeViewerFactory;
import naga.providers.toolkit.javafx.fx.viewer.FxButtonViewer;
import naga.providers.toolkit.javafx.fx.viewer.FxCheckBoxViewer;
import naga.toolkit.fx.scene.control.impl.ButtonImpl;
import naga.toolkit.fx.scene.control.impl.CheckBoxImpl;

/**
 * @author Bruno Salmon
 */
class JFoenixNodeViewerFactory extends FxNodeViewerFactory {

    final static JFoenixNodeViewerFactory SINGLETON = new JFoenixNodeViewerFactory();

    private JFoenixNodeViewerFactory() {
        registerNodeViewerFactory(ButtonImpl.class, () -> new FxButtonViewer() {
            @Override
            protected JFXButton createFxNode() {
                return new JFXButton();
            }
        });
        registerNodeViewerFactory(CheckBoxImpl.class, () -> new FxCheckBoxViewer() {
            @Override
            protected JFXCheckBox createFxNode() {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
                return checkBox;
            }
        });
    }
}
