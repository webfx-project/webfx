package naga.framework.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextDirectAccess;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContextDirectAccess<C extends DomainActivityContext<C>> extends ActivityContextDirectAccess<C>, DomainActivityContext<C> {

    @Override
    default C setDataSourceModel(DataSourceModel dataSourceModel) {
        return getActivityContext().setDataSourceModel(dataSourceModel);
    }

    @Override
    default DataSourceModel getDataSourceModel() {
        return getActivityContext().getDataSourceModel();
    }
}
