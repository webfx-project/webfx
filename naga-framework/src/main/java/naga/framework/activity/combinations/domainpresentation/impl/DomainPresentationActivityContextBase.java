package naga.framework.activity.combinations.domainpresentation.impl;

import naga.framework.activity.combinations.domainpresentation.DomainPresentationActivityContext;
import naga.framework.activity.domain.DomainActivityContext;
import naga.framework.activity.presentation.impl.PresentationActivityContextBase;
import naga.framework.activity.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.presentation.view.PresentationViewActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class DomainPresentationActivityContextBase
        <THIS extends DomainPresentationActivityContextBase<THIS, C1, C2, PM>,
                C1 extends PresentationViewActivityContext<C1, PM>,
                C2 extends PresentationLogicActivityContext<C2, PM>,
                PM>

       extends PresentationActivityContextBase<THIS, C1, C2, PM>
        implements DomainPresentationActivityContext<THIS, C1, C2, PM> {

    public DomainPresentationActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    private DataSourceModel dataSourceModel;

    @Override
    public THIS setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        return (THIS) this;
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
