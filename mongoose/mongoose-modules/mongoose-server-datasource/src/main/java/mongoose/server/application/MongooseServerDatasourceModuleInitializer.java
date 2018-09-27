package mongoose.server.application;

import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.datasource.LocalDataSourceRegistry;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.resource.ResourceService;

/**
 * @author Bruno Salmon
 */
public final class MongooseServerDatasourceModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "mongoose-server-datasource";
    }

    @Override
    public int getInitLevel() {
        return JOBS_START_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        Object dataSourceId = DomainModelSnapshotLoader.getDataSourceModel().getId();
        String jsonConnectionDetails = ResourceService.getText("mongoose/server/datasource/" + dataSourceId + "/ConnectionDetails.json").result();
        JsonObject jso = jsonConnectionDetails == null ? null : Json.parseObject(jsonConnectionDetails);
        ConnectionDetails connectionDetails = ConnectionDetails.fromJson(jso);
        if (connectionDetails != null) {
            LocalDataSourceRegistry.registerLocalDataSource(dataSourceId, connectionDetails);
            Logger.log("Registered Mongoose local data source " + dataSourceId);
        }
    }
}
