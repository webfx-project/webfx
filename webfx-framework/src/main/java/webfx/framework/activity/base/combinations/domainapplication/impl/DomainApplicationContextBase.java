package webfx.framework.activity.base.combinations.domainapplication.impl;

import webfx.framework.activity.base.combinations.domainapplication.DomainApplicationContext;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.activity.ActivityContextFactory;
import webfx.framework.activity.base.elementals.application.impl.ApplicationContextBase;

/**
 * @author Bruno Salmon
 */
public class DomainApplicationContextBase
        <THIS extends DomainApplicationContextBase<THIS>>

        extends ApplicationContextBase<THIS>
        implements DomainApplicationContext<THIS> {

    private DataSourceModel dataSourceModel;

    public DomainApplicationContextBase(String[] mainArgs, ActivityContextFactory contextFactory) {
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
