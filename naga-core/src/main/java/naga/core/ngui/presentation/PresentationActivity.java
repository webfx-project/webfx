package naga.core.ngui.presentation;

import naga.core.activity.Activity;
import naga.core.activity.ActivityContext;
import naga.core.ngui.rx.RxFilter;
import naga.core.spi.gui.GuiToolkit;
import naga.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationActivity<UM extends UiModel, PM extends PresentationModel> implements Activity {

    private Factory<PM> presentationModelFactory;
    private PM presentationModel;
    private UiBuilder<UM> uiBuilder;
    private UM uiModel;
    private ActivityContext activityContext;

    private boolean uiBoundWithPresentationModel;
    private boolean presentationModelBoundWithLogic;

    protected void setPresentationModelFactory(Factory<PM> presentationModelFactory) {
        this.presentationModelFactory = presentationModelFactory;
    }

    protected void setUiBuilder(UiBuilder<UM> uiBuilder) {
        this.uiBuilder = uiBuilder;
    }

    protected ActivityContext getActivityContext() {
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
        GuiToolkit toolkit = GuiToolkit.get();
        if (uiModel == null) {
            //Platform.log("Building UI model on resuming " + this.getClass());
            uiModel = uiBuilder != null ? uiBuilder.buildUiModel(toolkit) : buildUiModel(toolkit);
        }
        if (!uiBoundWithPresentationModel) {
            //Platform.log("Binding UI model with presentation model");
            bindUiModelWithPresentationModel(uiModel, presentationModel);
            uiBoundWithPresentationModel = true;
        }
        toolkit.scheduler().runInUiThread(() -> activityContext.setNode(uiModel.getContentNode()));
    }

    @Override
    public void onDestroy() {
        uiModel = null;
        uiBoundWithPresentationModel = false;
    }

    protected PM buildPresentationModel() { throw new RuntimeException();}

    protected void initializePresentationModel(PM pm) {}

    protected abstract void bindPresentationModelWithLogic(PM pm);

    protected abstract UM buildUiModel(GuiToolkit toolkit);

    protected abstract void bindUiModelWithPresentationModel(UM um, PM pm);

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
