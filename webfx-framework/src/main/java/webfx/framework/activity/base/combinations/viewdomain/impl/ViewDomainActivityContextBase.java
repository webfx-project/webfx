package webfx.framework.activity.base.combinations.viewdomain.impl;

import webfx.framework.activity.base.elementals.domain.DomainActivityContext;
import webfx.framework.activity.base.elementals.view.impl.ViewActivityContextBase;
import webfx.framework.activity.base.combinations.viewdomain.ViewDomainActivityContext;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class ViewDomainActivityContextBase
        <THIS extends ViewDomainActivityContextBase<THIS>>

        extends ViewActivityContextBase<THIS>
        implements ViewDomainActivityContext<THIS> {

    private DataSourceModel dataSourceModel;

    protected ViewDomainActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    @Override
    public THIS setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        return (THIS) this;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        if (dataSourceModel != null)
            return dataSourceModel;
        ActivityContext parentContext = getParentContext();
        if (parentContext instanceof DomainActivityContext)
            return ((DomainActivityContext) parentContext).getDataSourceModel();
        return null;
    }
}
