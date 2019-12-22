package webfx.framework.server.services.push.spi.impl.simple;

import webfx.framework.server.services.push.UnresponsivePushClientListener;
import webfx.framework.server.services.push.spi.PushServerServiceProvider;
import webfx.platform.shared.services.scheduler.Scheduled;
import webfx.platform.shared.services.scheduler.Scheduler;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.services.bus.Bus;
import webfx.platform.shared.services.buscall.BusCallService;
import webfx.platform.shared.services.bus.BusService;
import webfx.platform.shared.services.log.Logger;
import webfx.framework.shared.services.push.ClientPushBusAddressesSharedByBothClientAndServer;
import webfx.platform.shared.util.async.AsyncResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class SimplePushServerServiceProvider implements PushServerServiceProvider {

    private final static long PING_PUSH_PERIOD_MS = 20_000; // Should be lower than client WebSocketBusOptions.pingInterval (which is set to 30_000 at the time of writing this code)

    private final Map<Object /*pushClientId*/, PushClientInfo> pushClientInfos = new HashMap<>();
    private final List<UnresponsivePushClientListener> unresponsivePushClientListeners = new ArrayList<>();

    @Override
    public <T> Future<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId) {
        Future<T> future = Future.future();
        PushClientInfo pushClientInfo = getOrCreatePushClientInfo(pushClientId);
        String clientBusCallServiceAddress = ClientPushBusAddressesSharedByBothClientAndServer.computeClientBusCallServiceAddress(pushClientId);
        Logger.log("Pushing " + clientBusCallServiceAddress + " -> " + serviceAddress);
        pushClientInfo.touchCalled();
        BusCallService.call(clientBusCallServiceAddress, serviceAddress, javaArgument, bus).setHandler(ar -> {
            pushClientInfo.touchReceived(ar.cause());
            if (ar.failed())
                pushFailed(pushClientId);
            future.complete((AsyncResult<T>) ar);
        });
        return future;
    }

    @Override
    public void addUnresponsivePushClientListener(UnresponsivePushClientListener listener) {
        unresponsivePushClientListeners.add(listener);
    }

    @Override
    public void removeUnresponsivePushClientListener(UnresponsivePushClientListener listener) {
        unresponsivePushClientListeners.remove(listener);
    }

    private void firePushClientDisconnected(Object pushClientId) {
        for (UnresponsivePushClientListener listener : unresponsivePushClientListeners)
            listener.onUnresponsivePushClient(pushClientId);
    }

    private void pushFailed(Object pushClientId) {
        pushClientInfos.remove(pushClientId);
        firePushClientDisconnected(pushClientId);
    }

    private PushClientInfo getOrCreatePushClientInfo(Object pushClientId) {
        PushClientInfo pushClientInfo = pushClientInfos.get(pushClientId);
        if (pushClientInfo == null)
            pushClientInfos.put(pushClientId, pushClientInfo = new PushClientInfo(pushClientId));
        return pushClientInfo;
    }

    final class PushClientInfo {
        final Object pushClientId;
        int pendingCalls;
        long lastCallTime;
        long lastResultReceivedTime;
        Scheduled pingScheduled;

        PushClientInfo(Object pushClientId) {
            this.pushClientId = pushClientId;
        }

        void touchCalled() {
            pendingCalls++;
            lastCallTime = now();
            rescheduleNextPing();
        }

        void touchReceived(Throwable error) {
            pendingCalls--;
            lastResultReceivedTime = now();
            if (error == null)
                rescheduleNextPing();
            else {
                cancelNextPing();
                pushFailed(pushClientId);
            }
        }

        void rescheduleNextPing() {
            cancelNextPing();
            pingScheduled = Scheduler.scheduleDelay(PING_PUSH_PERIOD_MS, () -> pingPushClient(BusService.bus(), pushClientId));
        }

        void cancelNextPing() {
            if (pingScheduled != null)
                pingScheduled.cancel();
            pingScheduled = null;

        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }
}