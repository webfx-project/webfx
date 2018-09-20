package webfx.framework.activity.impl.combinations.domainapplication.impl;

import webfx.framework.activity.impl.combinations.domainapplication.DomainApplicationContext;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.activity.ActivityContextFactory;
import webfx.framework.activity.impl.elementals.application.impl.ApplicationContextBase;

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
