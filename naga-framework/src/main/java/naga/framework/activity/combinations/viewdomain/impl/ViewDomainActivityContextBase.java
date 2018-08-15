package naga.framework.activity.combinations.viewdomain.impl;

import naga.framework.activity.domain.DomainActivityContext;
import naga.framework.activity.view.impl.ViewActivityContextBase;
import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class ViewDomainActivityContextBase
        <THIS extends ViewDomainActivityContextBase<THIS>>

        extends ViewActivityContextBase<THIS>
        implements ViewDomainActivityContext<THIS> {

    private DataSourceModel dataSourceModel;

    protected ViewDomainActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

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
