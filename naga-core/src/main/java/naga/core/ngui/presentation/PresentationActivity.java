package naga.core.ngui.presentation;

import naga.core.activity.Activity;
import naga.core.activity.ActivityContext;
import naga.core.activity.ActivityContextDirectAccess;
import naga.core.ngui.rx.RxFilter;
import naga.core.spi.toolkit.Toolkit;
import naga.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationActivity<VM extends ViewModel, PM extends PresentationModel> implements Activity, ActivityContextDirectAccess {

    private Factory<PM> presentationModelFactory;
    private PM presentationModel;
    private ViewBuilder<VM> viewBuilder;
    private VM viewModel;
    private ActivityContext activityContext;

    private boolean viewBoundWithPresentationModel;
    private boolean presentationModelBoundWithLogic;

    protected PresentationActivity() {
    }

    protected PresentationActivity(Factory<PM> presentationModelFactory) {
        setPresentationModelFactory(presentationModelFactory);
    }

    protected void setPresentationModelFactory(Factory<PM> presentationModelFactory) {
        this.presentationModelFactory = presentationModelFactory;
    }

    protected void setViewBuilder(ViewBuilder<VM> viewBuilder) {
        this.viewBuilder = viewBuilder;
    }

    public ActivityContext getActivityContext() {
        return activityContext;
    }

    @Override
    public void onCreate(ActivityContext context) {
        this.activityContext = context;
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
    }

    @Override
    public void onResume() {
        initializePresentationModel(presentationModel); // Doing it again, in case the params have changed on a later resume
        Toolkit toolkit = Toolkit.get();
        if (viewModel == null) {
            //Platform.log("Building UI model on resuming " + this.getClass());
            viewModel = viewBuilder != null ? viewBuilder.buildUiModel(toolkit) : buildView(toolkit);
        }
        if (!viewBoundWithPresentationModel) {
            //Platform.log("Binding UI model with presentation model");
            bindViewModelWithPresentationModel(viewModel, presentationModel);
            viewBoundWithPresentationModel = true;
        }
        toolkit.scheduler().runInUiThread(() -> activityContext.setNode(viewModel.getContentNode()));
    }

    @Override
    public void onDestroy() {
        viewModel = null;
        viewBoundWithPresentationModel = false;
    }

    protected PM buildPresentationModel() { throw new RuntimeException();}

    protected void initializePresentationModel(PM pm) {}

    protected abstract void bindPresentationModelWithLogic(PM pm);

    protected abstract VM buildView(Toolkit toolkit);

    protected abstract void bindViewModelWithPresentationModel(VM vm, PM pm);

    /** Helpers **/

    protected RxFilter createRxFilter() {
        return initializeRxFilter(new RxFilter());
    }

    protected RxFilter createRxFilter(Object jsonOrClass) {
        return initializeRxFilter(new RxFilter(jsonOrClass));
    }

    private RxFilter initializeRxFilter(RxFilter rxFilter) {
        return rxFilter.setDataSourceModel(activityContext.getDataSourceModel());
    }
}
