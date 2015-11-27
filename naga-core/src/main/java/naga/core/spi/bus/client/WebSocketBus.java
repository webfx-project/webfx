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
package naga.core.spi.bus.client;

import naga.core.spi.bus.BusOptions;
import naga.core.spi.bus.Message;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.WebSocket;
import naga.core.util.async.Handler;

import java.util.HashMap;
import java.util.Map;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/WebSocketBus.java">Original Goodow class</a>
 */
@SuppressWarnings("rawtypes")
public class WebSocketBus extends SimpleBus {
    public static final String SESSION = "_session";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOPIC_LOGIN = "vertx.basicauthmanager.login";

    protected static final String BODY = "body";
    protected static final String TOPIC = "address";
    protected static final String REPLY_TOPIC = "replyAddress";
    protected static final String TYPE = "type";

    private final WebSocket.WebSocketHandler webSocketHandler;
    String serverUri;
    WebSocket webSocket;
    private int pingInterval;
    private Object pingTimerID = null;
    private String sessionId;
    private String username;
    private String password;
    final Map<String, Integer> handlerCount = new HashMap<>();

    public WebSocketBus(BusOptions options) {
        String serverUri = options.getProtocol() + (options.isServerSSL() ? "s://" : "://") + options.getServerHost() + ':' + options.getServerPort() + options.getBusPrefix();
        if (options.getProtocol().equals("ws"))
            serverUri += "/websocket";
        webSocketHandler = new WebSocket.WebSocketHandler() {
            @Override
            public void onOpen() {
                Platform.log("Connection open");
                sendLogin();
                // Send the first ping then send a ping every 5 seconds
                sendPing();
                cancelPingTimer();
                pingTimerID = Platform.schedulePeriodic(pingInterval, ignore -> sendPing());
                if (hook != null)
                    hook.handleOpened();
                publishLocal(ON_OPEN, null);
            }

            @Override
            public void onMessage(String msg) {
                //Platform.log("onMessage(), msg = " + msg);
                JsonObject json = Json.<JsonObject>parse(msg);
                @SuppressWarnings({"unchecked"})
                MessageImpl message = new MessageImpl(false, false, WebSocketBus.this, json.getString(TOPIC), json.getString(REPLY_TOPIC), json.get(BODY));
                internalHandleReceiveMessage(message);
            }

            @Override
            public void onError(String error) {
                Platform.log("Connection error = " + error);
                publishLocal(ON_ERROR, Json.createObject().set("message", error));
            }

            @Override
            public void onClose(JsonObject reason) {
                Platform.log("Connection closed, reason = " + reason);
                cancelPingTimer();
                publishLocal(ON_CLOSE, reason);
                if (hook != null)
                    hook.handlePostClose();
            }
        };

        connect(serverUri, options);
    }


    private void cancelPingTimer() {
        if (pingTimerID != null)
            Platform.cancelTimer(pingTimerID);
        pingTimerID = null;
    }

    public void connect(String serverUri, BusOptions options) {
        this.serverUri = serverUri;
        pingInterval = options.getPingInterval();
        sessionId = options.getSessionId();
        if (sessionId == null)
            sessionId = idGenerator.next(23);
        username = options.getUsername();
        password = options.getPassword();

        Platform.log("Connecting to " + serverUri);
        webSocket = Platform.createWebSocket(serverUri, options.getSocketOptions());
        webSocket.setListen(webSocketHandler);
    }

    @Override
    public WebSocket.State getReadyState() {
        return webSocket.getReadyState();
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    protected void doClose() {
        subscribeLocal(ON_CLOSE, event -> {
            clearHandlers();
            handlerCount.clear();
        });
        webSocket.close();
    }

    @Override
    protected boolean doSubscribe(boolean local, String topic, Handler<? extends Message> handler) {
        boolean subscribed = super.doSubscribe(local, topic, handler);
        if (local || !subscribed || (hook != null && !hook.handlePreSubscribe(topic, handler)))
            return false;
        if (handlerCount.containsKey(topic)) {
            handlerCount.put(topic, handlerCount.get(topic) + 1);
            return false;
        }
        handlerCount.put(topic, 1);
        sendSubscribe(topic);
        return true;
    }

    @Override
    protected <T> void doSendOrPub(boolean local, boolean send, String topic, Object msg, Handler<Message<T>> replyHandler) {
        checkNotNull(TOPIC, topic);
        if (local) {
            super.doSendOrPub(local, send, topic, msg, replyHandler);
            return;
        }
        JsonObject envelope = Json.createObject().set(TYPE, send ? "send" : "publish");
        envelope.set(TOPIC, topic).set(BODY, msg);
        if (replyHandler != null) {
            String replyTopic = makeUUID();
            envelope.set(REPLY_TOPIC, replyTopic);
            replyHandlers.put(replyTopic, (Handler) replyHandler);
        }
        send(envelope);
    }

    @Override
    protected boolean doUnsubscribe(boolean local, String topic, Handler<? extends Message> handler) {
        boolean unsubscribed = super.doUnsubscribe(local, topic, handler);
        if (local || !unsubscribed || (hook != null && !hook.handleUnsubscribe(topic)))
            return false;
        handlerCount.put(topic, handlerCount.get(topic) - 1);
        if (handlerCount.get(topic) == 0) {
            handlerCount.remove(topic);
            sendUnsubscribe(topic);
            return true;
        }
        return false;
    }

    protected void send(JsonObject msg) {
        if (getReadyState() != WebSocket.State.OPEN)
            throw new IllegalStateException("INVALID_STATE_ERR");
        webSocket.send(msg.toJsonString());
    }

    protected void sendLogin() {
        JsonObject msg = Json.createObject().set(SESSION, sessionId);
        if (username != null) {
            msg.set(USERNAME, username);
            if (password != null)
                msg.set(PASSWORD, password);
        }
        send(TOPIC_LOGIN, msg, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                if (message.body() != null && message.body().getNumber("code") != 0)
                    throw new RuntimeException(message.body().toJsonString());
            }
        });
    }

    protected void sendPing() {
        send(Json.createObject().set(TYPE, "ping"));
    }

    /*
     * First handler for this topic so we should register the connection
     */
    protected void sendSubscribe(String topic) {
        assert topic != null : "topic shouldn't be null";
        send(Json.createObject().set(TYPE, "register").set(TOPIC, topic));
    }

    /*
     * No more handlers so we should unregister the connection
     */
    protected void sendUnsubscribe(String topic) {
        send(Json.createObject().set(TYPE, "unregister").set(TOPIC, topic));
    }
}