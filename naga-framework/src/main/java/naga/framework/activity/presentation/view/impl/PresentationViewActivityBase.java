package naga.framework.activity.presentation.view.impl;

import javafx.scene.Node;
import naga.framework.activity.view.impl.ViewActivityBase;
import naga.framework.activity.presentation.view.PresentationViewActivity;
import naga.framework.activity.presentation.view.PresentationViewActivityContext;
import naga.framework.activity.presentation.view.PresentationViewActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationViewActivityBase
        <C extends PresentationViewActivityContext<C, PM>, PM>

        extends ViewActivityBase<C>
        implements PresentationViewActivity<C, PM>,
        PresentationViewActivityContextMixin<C, PM> {

    public PresentationViewActivityBase() {
    }

    public void setPresentationModel(PM presentationModel) {
        PresentationViewActivityContextBase.toViewModelActivityContextBase(getActivityContext()).setPresentationModel(presentationModel);
    }

    @Override
    public Node buildUi() {
        return buildPresentationView(getPresentationModel());
    }

    protected Node buildPresentationView(PM pm) {
        createViewNodes(pm);
        return assemblyViewNodes();
    }

    protected abstract void createViewNodes(PM pm);

    protected abstract Node assemblyViewNodes();

}
