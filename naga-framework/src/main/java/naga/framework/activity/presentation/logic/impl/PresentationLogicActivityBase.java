package naga.framework.activity.presentation.logic.impl;

import naga.commons.util.function.Factory;
import naga.framework.activity.presentation.logic.PresentationLogicActivity;
import naga.framework.activity.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.presentation.logic.PresentationLogicActivityContextMixin;
import naga.framework.activity.uiroute.impl.UiRouteActivityBase;

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

    private void setPresentationModel(PM presentationModel) {
        PresentationLogicActivityContextBase.of(getActivityContext()).setPresentationModel(presentationModel);
    }

    @Override
    public void onStart() {
        PM presentationModel = getPresentationModel();
        if (presentationModel == null)
            setPresentationModel(presentationModel = presentationModelFactory != null ? presentationModelFactory.create() : buildPresentationModel());
        initializePresentationModel(presentationModel);
        if (!presentationModelBoundWithLogic) {
            startLogic(presentationModel);
            presentationModelBoundWithLogic = true;
        }
        super.onStart(); // setting active to true
    }

    @Override
    public void onResume() {
        initializePresentationModel(getPresentationModel()); // Doing it again, in case the params have changed on a later resume
        super.onResume();
    }

    @Override
    public void onDestroy() {
        setPresentationModel(null);
        presentationModelBoundWithLogic = false;
    }

    protected PM buildPresentationModel() { return null;}

    protected void initializePresentationModel(PM pm) {}

    protected abstract void startLogic(PM pm);

}
