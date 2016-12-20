package naga.providers.toolkit.jfoenix;

import com.jfoenix.controls.JFXButton;
import naga.providers.toolkit.javafx.fx.viewer.FxButtonViewer;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class JFXButtonViewer
        <FxN extends JFXButton, N extends Button, NV extends ButtonViewerBase<N, NV, NM>, NM extends ButtonViewerMixin<N, NV, NM>>

        extends FxButtonViewer<FxN, N, NV, NM> {

    @Override
    protected FxN createFxNode() {
        return (FxN) new JFXButton();
    }
}
