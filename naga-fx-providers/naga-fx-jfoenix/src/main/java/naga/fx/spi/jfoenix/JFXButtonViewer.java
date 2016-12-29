package naga.fx.spi.jfoenix;

import com.jfoenix.controls.JFXButton;
import naga.fx.spi.javafx.fx.viewer.FxButtonViewer;
import naga.fx.scene.control.Button;
import naga.fx.spi.viewer.base.ButtonViewerBase;
import naga.fx.spi.viewer.base.ButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class JFXButtonViewer
        <FxN extends JFXButton, N extends Button, NB extends ButtonViewerBase<N, NB, NM>, NM extends ButtonViewerMixin<N, NB, NM>>

        extends FxButtonViewer<FxN, N, NB, NM> {

    @Override
    protected FxN createFxNode() {
        return (FxN) new JFXButton();
    }
}
