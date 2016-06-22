package naga.core.ui.presentation;

import naga.core.spi.toolkit.Toolkit;

/**
 * @author Bruno Salmon
 */
public interface ViewBuilder<VM extends ViewModel> {

    VM buildView(Toolkit toolkit);
}
