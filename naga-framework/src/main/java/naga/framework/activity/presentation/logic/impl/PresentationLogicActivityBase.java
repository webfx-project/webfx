package naga.framework.activity.presentation.logic.impl;

import naga.framework.activity.presentation.logic.PresentationLogicActivity;
import naga.framework.activity.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.presentation.logic.PresentationLogicActivityContextMixin;
import naga.framework.activity.uiroute.impl.UiRouteActivityBase;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationLogicActivityBase
        <C extends PresentationLogicActivityContext<C, PM>, PM>

        extends UiRouteActivityBase<C>
        implements PresentationLogicActivity<C, PM>,
        PresentationLogicActivityContextMixin<C, PM> {

    private Factory<PM> presentationModelFactory;
    private boolean presentationModelBoundWithLogic;

    protected PresentationLogicActivityBase() {
    }

    protected PresentationLogicActivityBase(Factory<PM> presentationModelFactory) {
        setPresentationModelFactory(presentationModelFactory);
    }

    protected void setPresentationModelFactory(Factory<PM> presentationModelFactory) {
        this.presentationModelFactory = presentationModelFactory;
    }

    protected PM createPresentationModel() { return presentationModelFactory.create();}

    protected void initializePresentationModel(PM pm) {}

    private void setPresentationModel(PM presentationModel) {
        PresentationLogicActivityContextBase.of(getActivityContext()).setPresentationModel(presentationModel);
    }

    @Override
    public void onCreate(C context) {
        super.onCreate(context);
        PM presentationModel = createPresentationModel();
        setPresentationModel(presentationModel);
        initializePresentationModel(presentationModel);
    }

    @Override
    public void onStart() {
        updateContextParametersFromRoute();
        if (!presentationModelBoundWithLogic) {
            startLogic(getPresentationModel());
            presentationModelBoundWithLogic = true;
        }
        super.onStart(); // setting active to true
    }

    protected abstract void startLogic(PM pm);

    @Override
    protected void updateModelFromContextParameters() {
        super.updateModelFromContextParameters();
        updatePresentationModelFromContextParameters(getPresentationModel());
    }

    protected void updatePresentationModelFromContextParameters(PM pm) {
    }

    @Override
    public void onDestroy() {
        setPresentationModel(null);
        presentationModelBoundWithLogic = false;
    }

}
