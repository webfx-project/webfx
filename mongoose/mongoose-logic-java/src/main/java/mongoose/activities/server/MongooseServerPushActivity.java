package mongoose.activities.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.activity.domain.DomainActivityContext;
import naga.framework.activity.domain.DomainActivityContextMixin;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.push.server.spi.PushServerService;
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

    private static long SERVER_PUSH_PULSE_PERIOD_MS = 10_000;
    private static long CLIENT_REPLY_TIMEOUT_MS = 30_000; // Same as BridgeOptions.DEFAULT_REPLY_TIMEOUT

    private DomainActivityContext activityContext;
    private Scheduled serverPushPeriodicTimer;
    private ServerPushPass runningServerPushPass;
    private boolean pulseOccurredDuringLastPass;

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
        serverPushPeriodicTimer = Scheduler.schedulePeriodic(SERVER_PUSH_PULSE_PERIOD_MS, this::pulse);
    }

    @Override
    public void onStop() {
        if (serverPushPeriodicTimer != null) {
            Logger.log("Stopping Mongoose server push activity...");
            serverPushPeriodicTimer.cancel();
        }
    }

    private synchronized void pulse() {
        pulseOccurredDuringLastPass = runningServerPushPass != null;
        if (!pulseOccurredDuringLastPass) {
            runningServerPushPass = new ServerPushPass();
            runningServerPushPass.run();
        }
    }

    private synchronized void onServerPushPassEnd() {
        runningServerPushPass = null;
        if (pulseOccurredDuringLastPass)
            pulse();
    }

    private class ServerPushPass implements Runnable {
        QueryResultSet rs;
        int rowCount;
        int replyCount;
        boolean[] replyReceived;
        Scheduled scheduled;

        @Override
        public void run() {
            QueryService.executeQuery(new QueryArgument("select id,process_id from session_connection where \"end\" is null", getDataSourceId())).setHandler(ar -> {
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
            if (rowCount <= 0)
                onServerPushPassEnd();
            else {
                replyReceived = new boolean[rowCount];
                for (int row = 0; row < rowCount; row++) {
                    Object processId = rs.getValue(row, 1);
                    final int r = row;
                    PushServerService.callClientService("serverPushClientListener", "Server push pulse for client process " + processId, Platform.bus(), processId).setHandler(ar -> {
                        replyReceived[r] = ar.succeeded();
                        if (++replyCount == rowCount && scheduled != null) {
                            scheduled.cancel();
                            recordConnectionEnds();
                        }
                    });
                }
                scheduled = Scheduler.scheduleDelay(CLIENT_REPLY_TIMEOUT_MS, this::recordConnectionEnds);
            }
        }

        private void recordConnectionEnds() {
            if (scheduled != null) {
                scheduled = null;
                StringBuilder sb = null;
                for (int row = 0; row < rowCount; row++)
                    if (!replyReceived[row]) {
                        if (sb == null)
                            sb = new StringBuilder("update session_connection set \"end\"=now() where id in (");
                        else
                            sb.append(", ");
                        sb.append(rs.<Object>getValue(row, 0));
                    }
                if (sb != null) {
                    sb.append(")");
                    Logger.log(sb);
                    UpdateService.executeUpdate(new UpdateArgument(sb.toString(), getDataSourceId())).setHandler(ar -> {
                        if (ar.failed())
                            Logger.log(ar.cause());
                        onServerPushPassEnd();
                    });
                } else
                    onServerPushPassEnd();
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
