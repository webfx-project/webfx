package naga.framework.activity.client;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class UiDomainActivityContextExtendable<THIS extends UiDomainActivityContextExtendable<THIS>> extends UiActivityContextExtendable<THIS> implements UiDomainActivityContext<THIS> {

    private DataSourceModel dataSourceModel;

    protected UiDomainActivityContextExtendable(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
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
        if (parentContext instanceof UiDomainActivityContext)
            return ((UiDomainActivityContext) parentContext).getDataSourceModel();
        return null;
    }
}
