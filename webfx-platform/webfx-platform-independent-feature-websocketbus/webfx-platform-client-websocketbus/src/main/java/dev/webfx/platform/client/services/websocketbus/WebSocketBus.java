/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
 */

/*
 * Copyright 2013 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package dev.webfx.platform.client.services.websocketbus;

import dev.webfx.platform.client.services.websocket.WebSocket;
import dev.webfx.platform.client.services.websocket.WebSocketListener;
import dev.webfx.platform.client.services.websocket.WebSocketService;
import dev.webfx.platform.shared.services.bus.Message;
import dev.webfx.platform.shared.services.bus.spi.impl.SimpleBus;
import dev.webfx.platform.shared.services.json.Json;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.json.WritableJsonObject;
import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.services.scheduler.Scheduled;
import dev.webfx.platform.shared.services.scheduler.Scheduler;
import dev.webfx.platform.shared.util.async.AsyncResult;
import dev.webfx.platform.shared.util.async.Handler;

import java.util.HashMap;
import java.util.Map;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/WebSocketBus.java">Original Goodow class</a>
 */
@SuppressWarnings("rawtypes")
public class WebSocketBus extends SimpleBus {

    private static final String BODY = "body";
    private static final String ADDRESS = "address";
    static final String REPLY_ADDRESS = "replyAddress";
    static final String TYPE = "type";

    private final WebSocketListener internalWebSocketHandler;
    String serverUri;
    WebSocket webSocket;
    private int pingInterval;
    private Scheduled pingScheduled;
    private String sessionId;
    final Map<String, Integer> handlerCount = new HashMap<>();
    // Possible external web socket listener to observe web socket connection state
    private WebSocketListener webSocketListener;

    WebSocketBus(WebSocketBusOptions options) {
        super(false);
        options.turnUnsetPropertiesToDefault(); // should be already done by the platform but just in case
        String serverUri =
                (options.getProtocol() == WebSocketBusOptions.Protocol.WS ? "ws" : "http")
                + (options.isServerSSL() ? "s://" : "://")
                + options.getServerHost()
                + ':' + options.getServerPort()
                + '/' + options.getBusPrefix()
                + (options.getProtocol() == WebSocketBusOptions.Protocol.WS ? "/websocket" : "");
        internalWebSocketHandler = new WebSocketListener() {
            @Override
            public void onOpen() {
                publishOnOpen();
                if (webSocketListener != null)
                    webSocketListener.onOpen();
            }

            @Override
            public void onMessage(String msg) {
                //Logger.log("Received message = " + msg);
                JsonObject json = Json.parseObject(msg);
                WebSocketBus.this.onMessage(json.getString(ADDRESS), json.getString(REPLY_ADDRESS), json.get(BODY));
                if (webSocketListener != null)
                    webSocketListener.onMessage(msg);
            }

            @Override
            public void onError(String error) {
                publishOnError(Json.createObject().set("message", error));
                if (webSocketListener != null)
                    webSocketListener.onError(error);
            }

            @Override
            public void onClose(JsonObject reason) {
                publishOnClose(reason);
                if (webSocketListener != null)
                    webSocketListener.onClose(reason);
            }
        };

        connect(serverUri, options);
    }

    public void setWebSocketListener(WebSocketListener webSocketListener) {
        this.webSocketListener = webSocketListener;
    }

    void connect(String serverUri, WebSocketBusOptions options) {
        this.serverUri = serverUri;
        pingInterval = options.getPingInterval();
        sessionId = options.getSessionId();
        if (sessionId == null)
            sessionId = makeUUID();

        if (webSocketListener == null)
            Logger.log("Connecting bus to " + serverUri);

        webSocket = WebSocketService.createWebSocket(serverUri, options.getSocketOptions());
        webSocket.setListener(internalWebSocketHandler);
    }

    WebSocket.State getReadyState() {
        return webSocket.getReadyState();
    }

    @Override
    public boolean isOpen() {
        return getReadyState() == WebSocket.State.OPEN;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    protected void doClose() {
        webSocket.close();
    }

    @Override
    protected void onOpen() {
        scheduleNextPing();
        super.onOpen();
        // sendLogin(); // Disabling the auto logic mechanism
    }

    @Override
    protected void onMessage(String address, String replyAddress, Object body) {
        touchMessageReceived();
        super.onMessage(address, replyAddress, body);
    }

    @Override
    protected void onClose(Object reason) {
        super.onClose(reason);
        cancelPingTimer();
        handlerCount.clear();
    }

    @Override
    protected boolean doSubscribe(boolean local, String address, Handler<? extends Message> handler) {
        boolean subscribed = super.doSubscribe(local, address, handler);
        if (local || !subscribed || (hook != null && !hook.handlePreSubscribe(address, handler)))
            return false;
        if (handlerCount.containsKey(address)) {
            handlerCount.put(address, handlerCount.get(address) + 1);
            return false;
        }
        handlerCount.put(address, 1);
        sendSubscribe(address);
        return true;
    }

    @Override
    protected <T> void doSendOrPub(boolean local, boolean send, String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        checkNotNull(ADDRESS, address);
        if (local) {
            super.doSendOrPub(local, send, address, msg, replyHandler);
            return;
        }
        WritableJsonObject envelope = Json.createObject().set(TYPE, send ? "send" : "publish").set(ADDRESS, address).set(BODY, msg);
        if (replyHandler != null) {
            String replyTopic = makeUUID();
            envelope.set(REPLY_ADDRESS, replyTopic);
            replyHandlers.put(replyTopic, (Handler) replyHandler);
        }
        send(envelope);
    }

    @Override
    protected <T> boolean doUnsubscribe(boolean local, String address, Handler<Message<T>> handler) {
        boolean unsubscribed = super.doUnsubscribe(local, address, handler);
        if (local || !unsubscribed || (hook != null && !hook.handleUnsubscribe(address)))
            return false;
        handlerCount.put(address, handlerCount.get(address) - 1);
        if (handlerCount.get(address) == 0) {
            handlerCount.remove(address);
            sendUnsubscribe(address);
            return true;
        }
        return false;
    }

    protected void send(JsonObject msg) {
        if (getReadyState() != WebSocket.State.OPEN)
            throw new IllegalStateException("INVALID_STATE_ERR");
        String data = msg.toJsonString();
        //Logger.log("Sending data: " + data);
        webSocket.send(data);
    }

/*
    protected void sendLogin() {
        WritableJsonObject msg = Json.createObject().set(SESSION, sessionId);
        if (username != null) {
            msg.set(USERNAME, username);
            if (password != null)
                msg.set(PASSWORD, password);
        }
        send(TOPIC_LOGIN, msg, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                if (message.body() != null && message.body().getInteger("code") != 0)
                    throw new RuntimeException(message.body().toJsonString());
            }
        });
    }
*/

    private void sendPing() {
        Logger.log("Sending client ping to server");
        send(Json.createObject().set(TYPE, "ping"));
        scheduleNextPing(); // in order to schedule the next ping
    }

    private void scheduleNextPing() {
        cancelPingTimer();
        pingScheduled = Scheduler.scheduleDelay(pingInterval, this::sendPing);
    }

    private void cancelPingTimer() {
        if (pingScheduled != null)
            pingScheduled.cancel();
        pingScheduled = null;
    }

    private void touchMessageReceived() {
        // Since we just received a message, we are sure the bus is connected so we can reschedule the next ping for
        // another new ping interval. In this way we can reduce the bus traffic (especially when there are lots of
        // clients). And if there is a server ping (such as the one provided by the push server service), this can
        // actually completely remove the client ping traffic.
        // FINALLY COMMENTED BECAUSE VERT.X (EVENT BUS BRIDGE) REQUIRES PING EVEN WHILE WEB SOCKET TRAFFIC
        // scheduleNextPing();
    }
    /*
     * First handler for this address so we should register the connection
     */
    void sendSubscribe(String address) {
        //assert address != null : "address shouldn't be null";
        send(Json.createObject().set(TYPE, "register").set(ADDRESS, address));
    }

    /*
     * No more handlers so we should unregister the connection
     */
    void sendUnsubscribe(String address) {
        send(Json.createObject().set(TYPE, "unregister").set(ADDRESS, address));
    }
}