package naga.core.ngui.presentation;

import naga.core.activity.Activity;
import naga.core.activity.ActivityContext;
import naga.core.spi.gui.GuiToolkit;
import naga.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationActivity<UM extends UiModel, PM extends PresentationModel> implements Activity {

    private Factory<PM> presentationModelFactory;
    private PM presentationModel;
    private Factory<UM> uiBuilder;
    private UM uiModel;
    private ActivityContext activityContext;

    private boolean uiBoundToPresentationModel;
    private boolean presentationModelBoundWithLogic;

    protected void setPresentationModelFactory(Factory<PM> presentationModelFactory) {
        this.presentationModelFactory = presentationModelFactory;
    }

    protected void setUiBuilder(Factory<UM> uiBuilder) {
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
        if (uiModel == null)
            uiModel = uiBuilder != null ? uiBuilder.create() : buildUiModel();
        if (!uiBoundToPresentationModel) {
            bindUiModelWithPresentationModel(uiModel, presentationModel);
            uiBoundToPresentationModel = true;
        }
        GuiToolkit.get().scheduler().runInUiThread(() -> activityContext.setNode(uiModel.getContentNode()));
    }

    @Override
    public void onDestroy() {
        uiModel = null;
        uiBoundToPresentationModel = false;
    }

    protected PM buildPresentationModel() { throw new RuntimeException();}

    protected void initializePresentationModel(PM pm) {}

    protected abstract void bindPresentationModelWithLogic(PM pm);

    protected abstract UM buildUiModel();

    protected abstract void bindUiModelWithPresentationModel(UM um, PM pm);

}
