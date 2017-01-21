package naga.framework.activity.view;

import naga.framework.activity.DomainActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

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
