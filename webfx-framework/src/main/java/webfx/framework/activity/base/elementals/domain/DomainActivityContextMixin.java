package webfx.framework.activity.base.elementals.domain;

import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.orm.domainmodel.HasDataSourceModel;
import webfx.framework.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContextMixin
        <C extends DomainActivityContext<C>>

        extends ActivityContextMixin<C>,
        DomainActivityContext<C>,
        HasDataSourceModel {

    @Override
    default C setDataSourceModel(DataSourceModel dataSourceModel) {
        return getActivityContext().setDataSourceModel(dataSourceModel);
    }

    @Override
    default DataSourceModel getDataSourceModel() {
        return getActivityContext().getDataSourceModel();
    }
}
