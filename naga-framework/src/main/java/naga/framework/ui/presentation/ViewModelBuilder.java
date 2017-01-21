package naga.framework.ui.presentation;

/**
 * @author Bruno Salmon
 */
public interface ViewModelBuilder<VM extends ViewModel> {

    VM buildViewModel();
}
