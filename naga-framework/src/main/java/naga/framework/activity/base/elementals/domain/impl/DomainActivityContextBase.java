package naga.framework.activity.base.elementals.domain.impl;

import naga.framework.activity.base.elementals.domain.DomainActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.activity.ActivityContext;
import naga.framework.activity.base.ActivityContextBase;
import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class DomainActivityContextBase
        <THIS extends DomainActivityContextBase<THIS>>

        extends ActivityContextBase<THIS>
        implements DomainActivityContext<THIS> {

    private DataSourceModel dataSourceModel;

    protected DomainActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    @Override
    public THIS setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        return (THIS) this;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }
}
