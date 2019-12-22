package webfx.framework.client.activity.impl.elementals.presentation.logic.impl;

import webfx.framework.client.activity.impl.elementals.presentation.logic.PresentationLogicActivity;
import webfx.framework.client.activity.impl.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.client.activity.impl.elementals.presentation.logic.PresentationLogicActivityContextMixin;
import webfx.framework.client.activity.impl.elementals.uiroute.impl.UiRouteActivityBase;
import webfx.platform.shared.util.function.Factory;

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
        // Because updating the model may result in changing several presentation model properties, the logic (ex: a
        // reactive expression mapper) may react by sending a query on each property change! To avoid this, we do a kind
        // of transaction by temporary setting the active property to false (no query should be sent for now)
        boolean active = isActive();
        setActive(false);
        super.updateModelFromContextParameters();
        updatePresentationModelFromContextParameters(getPresentationModel());
        // Restoring the initial active value
        setActive(active); // if active = true, this should act as a commit and a single final query will be sent
    }

    protected void updatePresentationModelFromContextParameters(PM pm) {
    }

    @Override
    public void onDestroy() {
        setPresentationModel(null);
        presentationModelBoundWithLogic = false;
    }

}
