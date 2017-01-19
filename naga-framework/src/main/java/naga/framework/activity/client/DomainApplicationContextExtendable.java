package naga.framework.activity.client;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.client.ApplicationContextExtendable;

/**
 * @author Bruno Salmon
 */
public class DomainApplicationContextExtendable<THIS extends DomainApplicationContextExtendable<THIS>> extends ApplicationContextExtendable<THIS> implements DomainApplicationContext<THIS> {

    private DataSourceModel dataSourceModel;

    public DomainApplicationContextExtendable(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
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
