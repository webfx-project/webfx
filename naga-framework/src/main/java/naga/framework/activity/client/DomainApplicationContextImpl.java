package naga.framework.activity.client;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.client.ApplicationContextImpl;

/**
 * @author Bruno Salmon
 */
public class DomainApplicationContextImpl<C extends DomainApplicationContextImpl<C>> extends ApplicationContextImpl<C> implements DomainApplicationContext<C> {

    private DataSourceModel dataSourceModel;

    public DomainApplicationContextImpl(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
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
