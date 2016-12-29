package naga.fx.spi.viewer.base;

import naga.fx.scene.control.TextInputControl;
import naga.fx.scene.text.Font;

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
