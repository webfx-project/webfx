package naga.core.ngui.presentation;

import naga.core.spi.toolkit.Toolkit;

/**
 * @author Bruno Salmon
 */
public interface UiBuilder<UM extends UiModel> {

    UM buildUiModel(Toolkit toolkit);
}
