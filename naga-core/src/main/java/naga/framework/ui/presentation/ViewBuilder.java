package naga.framework.ui.presentation;

import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public interface ViewBuilder<VM extends ViewModel> {

    VM buildView(Toolkit toolkit);
}
