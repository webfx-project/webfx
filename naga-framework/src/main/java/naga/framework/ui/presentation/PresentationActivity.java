package naga.framework.ui.presentation;

import javafx.scene.Node;
import naga.commons.util.function.Factory;
import naga.framework.activity.view.*;
import naga.framework.ui.filter.ReactiveExpressionFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationActivity<VM extends ViewModel, PM extends PresentationModel> extends ViewActivityBase<ViewDomainActivityContextFinal> implements ViewDomainActivityContextMixin<ViewDomainActivityContextFinal> {

    private Factory<PM> presentationModelFactory;
    private PM presentationModel;
    private VM viewModel;

    private boolean viewBoundWithPresentationModel;
    private boolean presentationModelBoundWithLogic;

    private static final Map<Class, ViewModelBuilder> viewBuilders = new HashMap<>();

    public static <VM extends ViewModel> void registerViewBuilder(Class<? extends PresentationActivity<VM, ? extends PresentationModel>> presentationActivityClass, ViewModelBuilder<VM> viewModelBuilder) {
        // Skipping any further registration attempt, keeping only the first one (typically the one defined at application top level)
        viewBuilders.putIfAbsent(presentationActivityClass, viewModelBuilder);
    }

    protected PresentationActivity() {
    }

    protected PresentationActivity(Factory<PM> presentationModelFactory) {
        setPresentationModelFactory(presentationModelFactory);
    }

    protected void setPresentationModelFactory(Factory<PM> presentationModelFactory) {
        this.presentationModelFactory = presentationModelFactory;
    }

    @Override
    public void onStart() {
        if (presentationModel == null)
            presentationModel = presentationModelFactory != null ? presentationModelFactory.create() : buildPresentationModel();
        initializePresentationModel(presentationModel);
        if (!presentationModelBoundWithLogic) {
            bindPresentationModelWithLogic(presentationModel);
            presentationModelBoundWithLogic = true;
        }
        super.onStart(); // setting active to true
    }

    @Override
    public void onResume() {
        initializePresentationModel(presentationModel); // Doing it again, in case the params have changed on a later resume
        super.onResume();
    }

    @Override
    public Node buildUi() {
        if (viewModel == null) {
            //Platform.log("Building UI model on resuming " + this.getClass());
            ViewModelBuilder<VM> viewModelBuilder = viewBuilders.get(getClass());
            viewModel = viewModelBuilder != null ? viewModelBuilder.buildViewModel() : buildView();
        }
        if (!viewBoundWithPresentationModel) {
            // Binding the mount node property so that child sub routed pages are displayed
            if (viewModel instanceof HasMountNodeProperty)
                ((HasMountNodeProperty) viewModel).mountNodeProperty().bind(mountNodeProperty());
            //Platform.log("Binding UI model with presentation model");
            bindViewModelWithPresentationModel(viewModel, presentationModel);
            viewBoundWithPresentationModel = true;
        }
        return viewModel.getContentNode();
    }

    @Override
    public void onDestroy() {
        viewModel = null;
        viewBoundWithPresentationModel = false;
    }

    protected PM buildPresentationModel() { return null;}

    protected void initializePresentationModel(PM pm) {}

    protected abstract void bindPresentationModelWithLogic(PM pm);

    protected VM buildView() {
        return null;
    }

    protected abstract void bindViewModelWithPresentationModel(VM vm, PM pm);

    /** Helpers **/

    protected ReactiveExpressionFilter createReactiveExpressionFilter() {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter());
    }

    protected ReactiveExpressionFilter createReactiveExpressionFilter(Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter(jsonOrClass));
    }

    private ReactiveExpressionFilter initializeReactiveExpressionFilter(ReactiveExpressionFilter reactiveExpressionFilter) {
        reactiveExpressionFilter.activePropertyProperty().bind(activeProperty());
        return reactiveExpressionFilter
                .setDataSourceModel(getActivityContext().getDataSourceModel())
                .setI18n(getI18n())
                ;
    }
}
