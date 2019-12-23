package mongoose.server.services.datasource;

import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.platform.shared.services.datasource.ConnectionDetails;
import webfx.platform.shared.services.datasource.DBMS;
import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.datasource.spi.LocalDataSourceProvider;
import webfx.platform.shared.services.datasource.spi.simple.SimpleLocalDataSource;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.resource.ResourceService;

/**
 * @author Bruno Salmon
 */
public final class MongooseLocalDataSourceProvider implements LocalDataSourceProvider {

    private final LocalDataSource MONGOOSE_DATA_SOURCE;

    public MongooseLocalDataSourceProvider() {
        Object dataSourceId = DataSourceModelService.getDefaultDataSourceId();
        DBMS dbms = DBMS.POSTGRES;
        String connectionPath = "mongoose/server/datasource/" + dataSourceId + "/ConnectionDetails.json";
        String connectionContent = ResourceService.getText(connectionPath).result();
        JsonObject json = connectionContent == null ? null : Json.parseObject(connectionContent);
        ConnectionDetails connectionDetails = json == null ? null : new ConnectionDetails(
                json.getString("host"),
                json.getInteger("port", -1),
                json.getString("filePath"),
                json.getString("databaseName"),
                json.getString("url"),
                json.getString("username"),
                json.getString("password")
        );
        if (connectionDetails == null)
            Logger.log("WARNING: No connection details found for Mongoose data source (please check " + connectionPath + ")");
        MONGOOSE_DATA_SOURCE = new SimpleLocalDataSource(dataSourceId, dbms, connectionDetails);
    }

    @Override
    public LocalDataSource getLocalDataSource(Object dataSourceId) {
        return MONGOOSE_DATA_SOURCE.getId().equals(dataSourceId) ? MONGOOSE_DATA_SOURCE : null;
    }
}
