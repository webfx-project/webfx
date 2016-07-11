package naga.framework.ui.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class UiDomainActivityContextImpl<C extends UiDomainActivityContextImpl<C>> extends UiActivityContextImpl<C> implements UiDomainActivityContext<C> {

    private DataSourceModel dataSourceModel;

    protected UiDomainActivityContextImpl(ActivityContext parentContext, ActivityContextFactory<C> contextFactory) {
        super(parentContext, contextFactory);
    }

    @Override
    public void setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        if (dataSourceModel != null)
            return dataSourceModel;
        ActivityContext parentContext = getParentContext();
        if (parentContext instanceof UiDomainActivityContext)
            return ((UiDomainActivityContext) parentContext).getDataSourceModel();
        return null;
    }
}
