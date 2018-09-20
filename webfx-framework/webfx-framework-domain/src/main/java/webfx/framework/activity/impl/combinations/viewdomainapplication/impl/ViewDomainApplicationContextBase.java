package webfx.framework.activity.impl.combinations.viewdomainapplication.impl;

import webfx.framework.activity.impl.combinations.viewapplication.impl.ViewApplicationContextBase;
import webfx.framework.activity.impl.combinations.viewdomainapplication.ViewDomainApplicationContext;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.activity.ActivityContextFactory;

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
