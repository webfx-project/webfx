package naga.framework.activity.view.presentation;

import naga.framework.activity.DomainActivityContext;
import naga.framework.activity.presentationlogic.DomainPresentationLogicActivityContextFinal;
import naga.framework.activity.view.presentationview.PresentationViewActivityContextFinal;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class DomainPresentationActivityContextFinal
       <PM>

        extends PresentationActivityContextBase<DomainPresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, DomainPresentationLogicActivityContextFinal<PM>, PM>
        implements DomainActivityContext<DomainPresentationActivityContextFinal<PM>> {


    public DomainPresentationActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, DomainPresentationActivityContextFinal::new);
    }

    public DomainPresentationActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<DomainPresentationActivityContextFinal<PM>> contextFactory) {
        super(parentContext, contextFactory);
    }

    private DataSourceModel dataSourceModel;

    @Override
    public DomainPresentationActivityContextFinal<PM> setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        return this;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        if (dataSourceModel != null)
            return dataSourceModel;
        ActivityContext parentContext = getParentContext();
        if (parentContext instanceof DomainActivityContext)
            return ((DomainActivityContext) parentContext).getDataSourceModel();
        return null;
    }
}
