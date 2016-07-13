package naga.framework.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContext<C extends DomainActivityContext<C>> extends ActivityContext<C> {

    C setDataSourceModel(DataSourceModel dataSourceModel);

    DataSourceModel getDataSourceModel();

    static DomainActivityContext create(ActivityContext parentContext) {
        return new DomainActivityContextImpl(parentContext, DomainActivityContext::create);
    }

    static DomainActivityContext create(ActivityContext parentContext, DataSourceModel dataSourceModel) {
        return create(parentContext).setDataSourceModel(dataSourceModel);
    }

    static DomainActivityContext create(DataSourceModel dataSourceModel) {
        return create(null, dataSourceModel);
    }

}
