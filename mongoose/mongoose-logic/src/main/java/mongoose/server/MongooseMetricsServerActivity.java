package mongoose.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import mongoose.entities.MetricsEntity;
import mongoose.services.Metrics;
import mongoose.services.MetricsService;
import naga.commons.scheduler.Scheduled;
import naga.framework.activity.DomainActivityContext;
import naga.framework.activity.DomainActivityContextDirectAccess;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.UpdateStore;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.update.UpdateArgument;
import naga.platform.spi.Platform;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Bruno Salmon
 */
public class MongooseMetricsServerActivity implements Activity<DomainActivityContext>, DomainActivityContextDirectAccess {

    private DomainActivityContext activityContext;
    private Scheduled metricsCapturePeriodicTimer;
    private Scheduled metricsCleaningPeriodicTimer;

    @Override
    public void onCreate(DomainActivityContext context) {
        this.activityContext = context;
    }

    public DomainActivityContext getActivityContext() {
        return activityContext;
    }

    @Override
    public void onStart() {
        // Getting the metrics service for this platform
        MetricsService metricsService = MetricsService.get();

        // Stopping the activity if there is actually no metrics service for this platform
        if (metricsService == null) {
            Platform.log("MongooseMetricsServerActivity will not start as no MetricsService is registered for this platform");
            getActivityManager().destroy(); // Asking the activity manager to stop and destroy this activity
            return;
        }

        Platform.log("Starting Mongoose metrics server activity...");
        // Starting a periodic timer to capture metrics every seconds and store it in the database
        metricsCapturePeriodicTimer = Platform.schedulePeriodic(1000, () -> {
            // Creating an update store for metrics entity
            UpdateStore store = UpdateStore.create(getDataSourceModel());
            // Instantiating a Metrics entity to be inserted in the database
            Metrics m = store.insertEntity(MetricsEntity.class);
            // Asking the metrics service to take a snapshot and store all values in the entity
            metricsService.takeMetricsSnapshot(m);
            // Asking the update store to record this in the database
            store.executeUpdate().setHandler(asyncResult -> {
                if (asyncResult.failed())
                    Platform.log("Inserting metrics in database failed!", asyncResult.cause());
            });
        });

        metricsCleaningPeriodicTimer = Platform.schedulePeriodic(3600 * 1000, () ->
            Platform.get().updateService().executeUpdate(new UpdateArgument("delete from metrics where lt_test_set_id is null and date < ?", new Object[]{Instant.now().minus(1, ChronoUnit.DAYS)}, false, getDataSourceModel().getId())).setHandler(asyncResult -> {
                if (asyncResult.failed())
                    Platform.log("Deleting metrics in database failed!", asyncResult.cause());
                else
                    Platform.log("" + asyncResult.result().getRowCount() + " metrics records have been deleted from the database");
            }));
    }

    @Override
    public void onStop() {
        if (metricsCapturePeriodicTimer != null) {
            Platform.log("Stopping Mongoose metrics server activity...");
            metricsCapturePeriodicTimer.cancel();
            metricsCapturePeriodicTimer = null;
            metricsCleaningPeriodicTimer.cancel();
            metricsCleaningPeriodicTimer = null;
        }
    }

    // Static method helper to start this activity

    public static void startActivity() {
        startActivity(new MongooseMetricsServerActivity());
    }

    public static void startActivity(MongooseMetricsServerActivity mongooseMetricsServerActivity) {
        DataSourceModel dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        String json = Platform.getResourceService().getText("mongoose/datasource/" + dataSourceModel.getId() + "/ConnectionDetails.json").result();
        JsonObject jso = json == null ? null : Json.parseObject(json);
        startActivity(mongooseMetricsServerActivity, dataSourceModel, ConnectionDetails.fromJson(jso));
    }

    public static void startActivity(MongooseMetricsServerActivity mongooseMetricsServerActivity, DataSourceModel dataSourceModel, ConnectionDetails connectionDetails) {
        if (connectionDetails != null)
            LocalDataSourceRegistry.registerLocalDataSource(dataSourceModel.getId(), connectionDetails);
        ActivityManager.startServerActivity(mongooseMetricsServerActivity, DomainActivityContext.create(dataSourceModel));
    }

}
