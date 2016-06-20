package naga.core.ui.presentation;

import naga.core.spi.toolkit.Toolkit;

/**
 * @author Bruno Salmon
 */
public interface ViewBuilder<UM extends ViewModel> {

    UM buildUiModel(Toolkit toolkit);
}
