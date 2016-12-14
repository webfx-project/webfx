package naga.framework.ui.presentation;

/**
 * @author Bruno Salmon
 */
public interface ViewBuilder<VM extends ViewModel> {

    VM buildView();
}
