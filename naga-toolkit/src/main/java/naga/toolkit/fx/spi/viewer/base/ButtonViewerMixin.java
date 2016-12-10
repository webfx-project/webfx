package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.viewer.ButtonViewer;

/**
 * @author Bruno Salmon
 */
public interface ButtonViewerMixin
        extends ButtonViewer,
        ButtonBaseViewerMixin<Button, ButtonViewerBase, ButtonViewerMixin> {
}
