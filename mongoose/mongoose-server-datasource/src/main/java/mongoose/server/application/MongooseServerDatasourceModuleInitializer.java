package mongoose.server.application;

import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.platform.shared.datasource.ConnectionDetails;
import webfx.platform.shared.datasource.LocalDataSourceRegistry;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.resource.ResourceService;

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
