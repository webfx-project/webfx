package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.control.TextInputControl;
import naga.toolkit.drawing.spi.view.TextInputControlView;
import naga.toolkit.drawing.text.Font;

/**
 * @author Bruno Salmon
 */
public interface TextInputControlViewMixin
        <N extends TextInputControl, NV extends TextInputControlViewBase<N, NV, NM>, NM extends TextInputControlViewMixin<N, NV, NM>>

        extends TextInputControlView<N>,
        ControlViewMixin<N, NV, NM> {

    void updateFont(Font font);

    void updateText(String text);

    void updatePrompt(String prompt);
}
