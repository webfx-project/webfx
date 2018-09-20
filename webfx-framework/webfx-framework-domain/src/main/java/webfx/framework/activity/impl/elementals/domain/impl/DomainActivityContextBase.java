package webfx.framework.activity.impl.elementals.domain.impl;

import webfx.framework.activity.impl.elementals.domain.DomainActivityContext;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.impl.ActivityContextBase;
import webfx.framework.activity.ActivityContextFactory;

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
