package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxButtonViewer
        extends FxButtonBaseViewer<javafx.scene.control.Button, Button, ButtonViewerBase, ButtonViewerMixin>
        implements ButtonViewerMixin, FxLayoutMeasurable {

    public FxButtonViewer() {
        super(new ButtonViewerBase());
    }

    @Override
    javafx.scene.control.Button createFxNode() {
        return new javafx.scene.control.Button();
    }
}
