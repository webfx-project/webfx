package naga.framework.activity.client;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class UiDomainApplicationContextExtendable
        <THIS extends UiDomainApplicationContextExtendable<THIS>>

        extends UiApplicationContextExtendable<THIS>
        implements UiDomainApplicationContext<THIS> {

    private DataSourceModel dataSourceModel;

    protected UiDomainApplicationContextExtendable(String[] mainArgs, ActivityContextFactory contextFactory) {
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
