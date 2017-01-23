package naga.framework.activity.domain;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContextMixin
        <C extends DomainActivityContext<C>>

        extends ActivityContextMixin<C>,
        DomainActivityContext<C> {

    @Override
    default C setDataSourceModel(DataSourceModel dataSourceModel) {
        return getActivityContext().setDataSourceModel(dataSourceModel);
    }

    @Override
    default DataSourceModel getDataSourceModel() {
        return getActivityContext().getDataSourceModel();
    }
}
