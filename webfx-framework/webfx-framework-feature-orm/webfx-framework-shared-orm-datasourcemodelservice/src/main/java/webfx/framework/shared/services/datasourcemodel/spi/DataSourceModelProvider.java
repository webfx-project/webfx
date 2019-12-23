package webfx.framework.shared.services.datasourcemodel.spi;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface DataSourceModelProvider {

    Object getDefaultDataSourceId();

    default DataSourceModel getDefaultDataSourceModel() {
        return getDataSourceModel(getDefaultDataSourceId());
    }

    DataSourceModel getDataSourceModel(Object dataSourceId);

    Future<DataSourceModel> loadDataSourceModel(Object dataSourceId);

}
