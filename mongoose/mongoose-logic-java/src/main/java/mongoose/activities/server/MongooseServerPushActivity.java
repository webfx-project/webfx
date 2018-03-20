package mongoose.activities.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.activity.domain.DomainActivityContext;
import naga.framework.activity.domain.DomainActivityContextMixin;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.spi.UpdateService;
import naga.platform.spi.Platform;
import naga.scheduler.Scheduled;
import naga.scheduler.Scheduler;

/**
 * @author Bruno Salmon
 */
public class MongooseServerPushActivity implements Activity<DomainActivityContext>, DomainActivityContextMixin {

    private DomainActivityContext activityContext;
    private Scheduled serverPushPeriodicTimer;

    @Override
    public void onCreate(DomainActivityContext context) {
        activityContext = context;
    }

    @Override
    public DomainActivityContext getActivityContext() {
        return activityContext;
    }

    @Override
    public void onStart() {
        Logger.log("Starting Mongoose server push activity...");
        // Starting a periodic timer to
        serverPushPeriodicTimer = Scheduler.schedulePeriodic(10_000, this::runServerPushPass);
    }

    @Override
    public void onStop() {
        if (serverPushPeriodicTimer != null) {
            Logger.log("Stopping Mongoose server push activity...");
            serverPushPeriodicTimer.cancel();
        }
    }

    private void runServerPushPass() {
        new ServerPushPass().run();
    }

    private class ServerPushPass implements Runnable {
        private QueryResultSet rs;
        int rowCount;
        int replyCount;
        boolean[] replyReceived;
        Scheduled scheduled;

        @Override
        public void run() {
            QueryService.executeQuery(new QueryArgument("select id,process_id from session_connection where \"end\" is null", getDataSourceModel().getId())).setHandler(ar -> {
                if (ar.failed())
                    Logger.log(ar.cause());
                else {
                    rs = ar.result();
                    sendServerPush();
                }
            });

        }

        private void sendServerPush() {
            rowCount = rs.getRowCount();
            if (rowCount > 0) {
                replyReceived = new boolean[rowCount];
                for (int row = 0; row < rowCount; row++) {
                    Object processId = rs.getValue(row, 1);
                    final int r = row;
                    String clientAddress = "client/" + processId;
                    Platform.bus().send(clientAddress, "Server push pulse for " + clientAddress, ar2 -> {
                        replyReceived[r] = ar2.succeeded();
                        if (++replyCount == rowCount && scheduled != null) {
                            scheduled.cancel();
                            recordConnectionEnds();
                        }
                    });
                }
                scheduled = Scheduler.scheduleDelay(30_000 /* Same as BridgeOptions.DEFAULT_REPLY_TIMEOUT */, this::recordConnectionEnds);
            }
        }

        private void recordConnectionEnds() {
            if (scheduled != null) {
                scheduled = null;
                StringBuilder sb = null;
                for (int row = 0; row < rowCount; row++) {
                    if (!replyReceived[row]) {
                        if (sb == null)
                            sb = new StringBuilder("update session_connection set \"end\"=now() where id in (");
                        else
                            sb.append(", ");
                        sb.append(rs.<Object>getValue(row, 0));
                    }
                }
                if (sb != null) {
                    sb.append(")");
                    Logger.log(sb);
                    UpdateService.executeUpdate(new UpdateArgument(sb.toString(), null, false, getDataSourceModel().getId())).setHandler(ar2 -> {
                        if (ar2.failed())
                            Logger.log(ar2.cause());
                    });
                }
            }
        }
    }

    // Static method helper to start this activity

    public static void startActivity() {
        startActivity(new MongooseServerPushActivity());
    }

    private static void startActivity(MongooseServerPushActivity mongooseServerPushActivity) {
        DataSourceModel dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        startActivity(mongooseServerPushActivity, dataSourceModel);
    }

    private static void startActivity(MongooseServerPushActivity mongooseServerPushActivity, DataSourceModel dataSourceModel) {
        ActivityManager.startServerActivity(mongooseServerPushActivity, DomainActivityContext.createDomainActivityContext(dataSourceModel));
    }

}
