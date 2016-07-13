package naga.framework.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ActivityContextImpl;

/**
 * @author Bruno Salmon
 */
public class DomainActivityContextImpl<C extends DomainActivityContextImpl<C>> extends ActivityContextImpl<C> implements DomainActivityContext<C> {

    private DataSourceModel dataSourceModel;

    protected DomainActivityContextImpl(ActivityContext parentContext, ActivityContextFactory<C> contextFactory) {
        super(parentContext, contextFactory);
    }

    @Override
    public C setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        return (C) this;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }
}
