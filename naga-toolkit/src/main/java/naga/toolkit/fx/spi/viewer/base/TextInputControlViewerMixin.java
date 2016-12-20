package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.TextInputControl;
import naga.toolkit.fx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public interface TextInputControlViewerMixin
        <N extends TextInputControl, NB extends TextInputControlViewerBase<N, NB, NM>, NM extends TextInputControlViewerMixin<N, NB, NM>>

        extends ControlViewerMixin<N, NB, NM> {

    void updateFont(Font font);

    void updateText(String text);

    void updatePrompt(String prompt);
}
