package naga.core.ngui.presentation;

import naga.core.spi.gui.GuiToolkit;

/**
 * @author Bruno Salmon
 */
public interface UiBuilder<UM extends UiModel> {

    UM buildUiModel(GuiToolkit toolkit);
}
