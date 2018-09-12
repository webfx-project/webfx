package webfx.platforms.core.services.push.server.spi.impl;

import webfx.platforms.core.services.bus.Bus;
import webfx.platforms.core.services.bus.call.BusCallService;
import webfx.platforms.core.services.bus.spi.BusService;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.push.ClientPushBusAddressesSharedByBothClientAndServer;
import webfx.platforms.core.services.push.server.PushClientDisconnectListener;
import webfx.platforms.core.services.push.server.spi.PushServerServiceProvider;
import webfx.platforms.core.services.scheduler.Scheduled;
import webfx.platforms.core.services.scheduler.Scheduler;
import webfx.platforms.core.util.async.AsyncResult;
import webfx.platforms.core.util.async.Future;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class PushServerServiceProviderImpl implements PushServerServiceProvider {

    private final static long PING_PUSH_PERIOD_MS = 20_000; // Should be lower than client WebSocketBusOptions.pingInterval (which is set to 30_000 at the time of writing this code)

    private final Map<Object /*pushClientId*/, PushClientInfo> pushClientInfos = new HashMap<>();
    private final List<PushClientDisconnectListener> pushClientDisconnectListeners = new ArrayList<>();

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
    public void addPushClientDisconnectListener(PushClientDisconnectListener listener) {
        pushClientDisconnectListeners.add(listener);
    }

    @Override
    public void removePushClientDisconnectListener(PushClientDisconnectListener listener) {
        pushClientDisconnectListeners.remove(listener);
    }

    private void firePushClientDisconnected(Object pushClientId) {
        for (PushClientDisconnectListener listener : pushClientDisconnectListeners)
            listener.pushClientDisconnected(pushClientId);
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

    class PushClientInfo {
        final Object pushClientId;
        int pendingCalls;
        long lastCallTime;
        long lastResultReceivedTime;
        Throwable error;
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