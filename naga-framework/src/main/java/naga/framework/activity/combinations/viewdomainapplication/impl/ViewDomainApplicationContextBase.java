package naga.framework.activity.combinations.viewdomainapplication.impl;

import naga.framework.activity.combinations.viewapplication.impl.ViewApplicationContextBase;
import naga.framework.activity.combinations.viewdomainapplication.ViewDomainApplicationContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class ViewDomainApplicationContextBase
        <THIS extends ViewDomainApplicationContextBase<THIS>>

        extends ViewApplicationContextBase<THIS>
        implements ViewDomainApplicationContext<THIS> {

    private DataSourceModel dataSourceModel;

    protected ViewDomainApplicationContextBase(String[] mainArgs, ActivityContextFactory contextFactory) {
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
