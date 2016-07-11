package naga.framework.ui.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ActivityContextImpl;

/**
 * @author Bruno Salmon
 */
public class DomainActivityContextImpl<C extends DomainActivityContextImpl<C>> extends ActivityContextImpl<C> implements DomainActivityContext<C> {

    protected DomainActivityContextImpl(ActivityContext parentContext, ActivityContextFactory<C> contextFactory) {
        super(parentContext, contextFactory);
    }

    @Override
    public void setDataSourceModel(DataSourceModel dataSourceModel) {

    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return null;
    }
}
