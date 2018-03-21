package mongoose.activities.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import mongoose.entities.MetricsEntity;
import mongoose.spi.metrics.Metrics;
import mongoose.spi.metrics.MetricsService;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.update.spi.UpdateService;
import naga.scheduler.Scheduled;
import naga.framework.activity.domain.DomainActivityContext;
import naga.framework.activity.domain.DomainActivityContextMixin;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.UpdateStore;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.services.update.UpdateArgument;
import naga.scheduler.Scheduler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Bruno Salmon
 */
public class MongooseServerMetricsActivity implements Activity<DomainActivityContext>, DomainActivityContextMixin {

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
            Logger.log("MongooseServerMetricsActivity will not start as no MetricsService is registered for this platform");
            getActivityManager().destroy(); // Asking the activity manager to stop and destroy this activity
            return;
        }

        Logger.log("Starting Mongoose metrics server activity...");
        // Starting a periodic timer to capture metrics every seconds and store it in the database
        metricsCapturePeriodicTimer = Scheduler.schedulePeriodic(1000, () -> {
            // Creating an update store for metrics entity
            UpdateStore store = UpdateStore.create(getDataSourceModel());
            // Instantiating a Metrics entity to be inserted in the database
            Metrics m = store.insertEntity(MetricsEntity.class);
            // Asking the metrics service to take a snapshot and store all values in the entity
            metricsService.takeMetricsSnapshot(m);
            // Asking the update store to record this in the database
            store.executeUpdate().setHandler(asyncResult -> {
                if (asyncResult.failed())
                    Logger.log("Inserting metrics in database failed!", asyncResult.cause());
            });
        });

        metricsCleaningPeriodicTimer = Scheduler.schedulePeriodic(12 * 3600 * 1000, () ->
            UpdateService.executeUpdate(new UpdateArgument("delete from metrics where lt_test_set_id is null and date < ?", new Object[]{Instant.now().minus(1, ChronoUnit.DAYS)}, getDataSourceId())).setHandler(ar -> {
                if (ar.failed())
                    Logger.log("Deleting metrics in database failed!", ar.cause());
                else
                    Logger.log("" + ar.result().getRowCount() + " metrics records have been deleted from the database");
            }));
    }

    @Override
    public void onStop() {
        if (metricsCapturePeriodicTimer != null) {
            Logger.log("Stopping Mongoose metrics server activity...");
            metricsCapturePeriodicTimer.cancel();
            metricsCapturePeriodicTimer = null;
            metricsCleaningPeriodicTimer.cancel();
            metricsCleaningPeriodicTimer = null;
        }
    }

    // Static method helper to start this activity

    public static void startActivity() {
        startActivity(new MongooseServerMetricsActivity());
    }

    private static void startActivity(MongooseServerMetricsActivity mongooseServerMetricsActivity) {
        DataSourceModel dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        startActivity(mongooseServerMetricsActivity, dataSourceModel);
    }

    private static void startActivity(MongooseServerMetricsActivity mongooseServerMetricsActivity, DataSourceModel dataSourceModel) {
        ActivityManager.startServerActivity(mongooseServerMetricsActivity, DomainActivityContext.createDomainActivityContext(dataSourceModel));
    }

}
