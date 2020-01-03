package mongoose.server.jobs.systemmetrics;

import mongoose.server.services.systemmetrics.SystemMetricsService;
import mongoose.shared.entities.SystemMetricsEntity;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.scheduler.Scheduled;
import webfx.platform.shared.services.scheduler.Scheduler;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Bruno Salmon
 */
public final class SystemMetricsRecorderJob implements ApplicationJob {

    private Scheduled metricsCapturePeriodicTimer;
    private Scheduled metricsCleaningPeriodicTimer;


    @Override
    public void onStart() {
        // Checking there is a metrics service provider registered for this platform
        if (SystemMetricsService.getProvider() == null)
            throw new IllegalStateException("SystemMetricsRecorderJob will not start as no SystemMetricsServiceProvider is registered for this platform");

        DataSourceModel dataSourceModel = DataSourceModelService.getDefaultDataSourceModel();
        // Starting a periodic timer to capture metrics every seconds and record it in the database
        metricsCapturePeriodicTimer = Scheduler.schedulePeriodic(1000, () -> {
            // Creating an update store for metrics entity
            UpdateStore store = UpdateStore.create(dataSourceModel);
            // Instantiating a new system metrics entity and asking the system metrics service to fill that entity
            SystemMetricsService.takeSystemMetricsSnapshot(store.insertEntity(SystemMetricsEntity.class));
            // Submitting this new record into the database
            store.submitChanges().setHandler(asyncResult -> {
                if (asyncResult.failed())
                    Logger.log("Inserting metrics in database failed!", asyncResult.cause());
            });
        });

        // Deleting old metrics records (older than 1 day) regularly (every 12h)
        metricsCleaningPeriodicTimer = Scheduler.schedulePeriodic(12 * 3600 * 1000, () ->
                SubmitService.executeSubmit(SubmitArgument.builder()
                        .setLanguage("DQL")
                        .setStatement("delete Metrics where LtTestSet is null and date < ?")
                        .setParameters(Instant.now().minus(1, ChronoUnit.DAYS))
                        .setDataSourceId(dataSourceModel.getDataSourceId())
                        .build()
                ).setHandler(ar -> {
                    if (ar.failed())
                        Logger.log("Deleting metrics in database failed!", ar.cause());
                    else
                        Logger.log("" + ar.result().getRowCount() + " metrics records have been deleted from the database");
                }));
    }

    @Override
    public void onStop() {
        if (metricsCapturePeriodicTimer != null)
            metricsCapturePeriodicTimer.cancel();
        if (metricsCapturePeriodicTimer != null)
            metricsCleaningPeriodicTimer.cancel();
        metricsCapturePeriodicTimer = metricsCleaningPeriodicTimer = null;
    }
}
