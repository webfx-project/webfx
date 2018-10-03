package webfx.framework.client.activity.impl.elementals.domain.impl;

import webfx.framework.client.activity.impl.elementals.domain.DomainActivityContext;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.impl.ActivityContextBase;
import webfx.framework.client.activity.ActivityContextFactory;

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
