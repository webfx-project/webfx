package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.TextInputControl;
import naga.toolkit.fx.spi.viewer.TextInputControlViewer;
import naga.toolkit.fx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public interface TextInputControlViewerMixin
        <N extends TextInputControl, NV extends TextInputControlViewerBase<N, NV, NM>, NM extends TextInputControlViewerMixin<N, NV, NM>>

        extends TextInputControlViewer<N>,
        ControlViewerMixin<N, NV, NM> {

    void updateFont(Font font);

    void updateText(String text);

    void updatePrompt(String prompt);
}
