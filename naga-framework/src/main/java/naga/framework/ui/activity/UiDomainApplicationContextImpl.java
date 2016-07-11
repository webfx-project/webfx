package naga.framework.ui.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class UiDomainApplicationContextImpl<C extends UiDomainApplicationContextImpl<C>> extends UiApplicationContextImpl<C> implements UiDomainApplicationContext<C> {

    private DataSourceModel dataSourceModel;

    protected UiDomainApplicationContextImpl(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }

    @Override
    public void setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }
}
