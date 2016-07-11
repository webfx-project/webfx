package naga.framework.ui.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextDirectAccess;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContextDirectAccess<C extends DomainActivityContext<C>> extends ActivityContextDirectAccess<C>, DomainActivityContext<C> {

    @Override
    default void setDataSourceModel(DataSourceModel dataSourceModel) {
        getActivityContext().setDataSourceModel(dataSourceModel);
    }

    @Override
    default DataSourceModel getDataSourceModel() {
        return getActivityContext().getDataSourceModel();
    }
}
