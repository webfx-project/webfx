package naga.framework.ui.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContext<C extends DomainActivityContext<C>> extends ActivityContext<C> {

    void setDataSourceModel(DataSourceModel dataSourceModel);

    DataSourceModel getDataSourceModel();

    static DomainActivityContext create(ActivityContext parentContext) {
        return new DomainActivityContextImpl(parentContext, DomainActivityContext::create);
    }

}
