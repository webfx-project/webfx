package webfx.framework.shared.services.datasourcemodel;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.datasourcemodel.spi.DataSourceModelProvider;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class DataSourceModelService {

    public static DataSourceModelProvider getProvider() {
        return SingleServiceProvider.getProvider(DataSourceModelProvider.class, () -> ServiceLoader.load(DataSourceModelProvider.class));
    }

    public static Object getDefaultDataSourceId() {
        return getProvider().getDefaultDataSourceId();
    }

    public static DataSourceModel getDefaultDataSourceModel() {
        return getProvider().getDefaultDataSourceModel();
    }

    public static Future<DataSourceModel> loadDataSourceModel(Object dataSourceId) {
        return getProvider().loadDataSourceModel(dataSourceId);
    }

    public static DataSourceModel getDataSourceModel(Object dataSourceId) {
        return getProvider().getDataSourceModel(dataSourceId);
    }
}
