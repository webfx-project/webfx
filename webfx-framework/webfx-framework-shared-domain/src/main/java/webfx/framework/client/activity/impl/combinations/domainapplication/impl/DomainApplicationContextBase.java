package webfx.framework.client.activity.impl.combinations.domainapplication.impl;

import webfx.framework.client.activity.impl.combinations.domainapplication.DomainApplicationContext;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.client.activity.ActivityContextFactory;
import webfx.framework.client.activity.impl.elementals.application.impl.ApplicationContextBase;

/**
 * @author Bruno Salmon
 */
public class DomainApplicationContextBase
        <THIS extends DomainApplicationContextBase<THIS>>

        extends ApplicationContextBase<THIS>
        implements DomainApplicationContext<THIS> {

    private DataSourceModel dataSourceModel;

    public DomainApplicationContextBase(ActivityContextFactory contextFactory) {
        super(contextFactory);
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
