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

import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusHook;
import naga.core.spi.bus.Message;
import naga.core.spi.bus.Registration;
import naga.core.spi.json.Json;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.WebSocket;
import naga.core.util.async.Handler;
import naga.core.util.idgen.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/SimpleBus.java">Original Goodow class</a>
 */
@SuppressWarnings("rawtypes")
public class SimpleBus implements Bus {
    private static final Logger log = Logger.getLogger(SimpleBus.class.getName());

    static void checkNotNull(String paramName, Object param) {
        if (param == null)
            throw new IllegalArgumentException("Parameter " + paramName + " must be specified");
    }

    private Map<String, List<Handler<Message>>> handlerMap = new HashMap<>();
    final Map<String, Handler<Message>> replyHandlers = new HashMap<>();
    final IdGenerator idGenerator = new IdGenerator();
    BusHook hook;

    public SimpleBus() {
    }

    @Override
    public void close() {
        if (hook == null || hook.handlePreClose())
            doClose();
    }

    @Override
    public WebSocket.State getReadyState() {
        return handlerMap == null ? WebSocket.State.CLOSED : WebSocket.State.OPEN;
    }

    @Override
    public String getSessionId() {
        return "@";
    }

    @Override
    public Bus publish(String topic, Object msg) {
        return internalHandleSendOrPub(false, false, topic, msg, null);
    }

    @Override
    public Bus publishLocal(String topic, Object msg) {
        return internalHandleSendOrPub(true, false, topic, msg, null);
    }

    @Override
    public <T> Registration subscribe(String topic, Handler<Message<T>> handler) {
        return subscribeImpl(false, topic, handler);
    }

    @Override
    public <T> Registration subscribeLocal(String topic, Handler<Message<T>> handler) {
        return subscribeImpl(true, topic, handler);
    }

    @Override
    public <T> Bus send(String topic, Object msg, Handler<Message<T>> replyHandler) {
        return internalHandleSendOrPub(false, true, topic, msg, replyHandler);
    }

    @Override
    public <T> Bus sendLocal(String topic, Object msg, Handler<Message<T>> replyHandler) {
        return internalHandleSendOrPub(true, true, topic, msg, replyHandler);
    }

    @Override
    public Bus setHook(BusHook hook) {
        this.hook = hook;
        return this;
    }

    protected void doClose() {
        publishLocal(Bus.ON_CLOSE, null);
        clearHandlers();
        if (hook != null)
            hook.handlePostClose();
    }

    protected boolean doSubscribe(boolean local, String topic, Handler<? extends Message> handler) {
        checkNotNull("topic", topic);
        checkNotNull("handler", handler);
        List<Handler<Message>> handlers = handlerMap.get(topic);
        if (handlers != null && handlers.contains(handler))
            return false;
        if (handlers == null)
            handlerMap.put(topic, handlers = new ArrayList<>());
        handlers.add((Handler) handler);
        return true;
    }

    @SuppressWarnings("unchecked")
    protected <T> void doSendOrPub(boolean local, boolean send, String topic, Object msg, Handler<Message<T>> replyHandler) {
        checkNotNull("topic", topic);
        String replyTopic = null;
        if (replyHandler != null) {
            replyTopic = makeUUID();
            replyHandlers.put(replyTopic, (Handler) replyHandler);
        }
        MessageImpl message = new MessageImpl(local, send, this, topic, replyTopic, msg);
        if (!internalHandleReceiveMessage(message) && replyTopic != null)
            replyHandlers.remove(replyTopic);
    }

    protected boolean doUnsubscribe(boolean local, String topic, Handler<? extends Message> handler) {
        checkNotNull("topic", topic);
        checkNotNull("handler", handler);
        List<Handler<Message>> handlers = handlerMap.get(topic);
        if (handlers == null)
            return false;
        boolean removed = handlers.remove(handler);
        if (handlers.isEmpty())
            handlerMap.remove(topic);
        return removed;
    }

    void clearHandlers() {
        replyHandlers.clear();
        handlerMap.clear();
        handlerMap = null;
    }

    boolean internalHandleReceiveMessage(Message message) {
        if (message.isLocal() || hook == null || hook.handleReceiveMessage(message)) {
            doReceiveMessage(message);
            return true;
        }
        return false;
    }

    <T> Bus internalHandleSendOrPub(boolean local, boolean send, String topic, Object msg, Handler<Message<T>> replyHandler) {
        if (local || hook == null || hook.handleSendOrPub(send, topic, msg, replyHandler))
            doSendOrPub(local, send, topic, msg, replyHandler);
        return this;
    }

    String makeUUID() {
        return idGenerator.next(36);
    }

    private void doReceiveMessage(Message message) {
        String topic = message.topic();
        List<Handler<Message>> handlers = handlerMap.get(topic);
        if (handlers != null) {
            // We make a copy since the handler might get unregistered from within the handler itself,
            // which would screw up our iteration
            List<Handler<Message>> copy = new ArrayList<>(handlers);
            // Drain any messages that came in while the channel was not open.
            for (Handler<Message> handler : copy)
                scheduleHandle(topic, handler, message);
        } else {
            // Might be a reply message
            Handler<Message> handler = replyHandlers.get(topic);
            if (handler != null) {
                replyHandlers.remove(topic);
                scheduleHandle(topic, handler, message);
            }
        }
    }

    private void handle(String topic, Handler<Message> handler, Message message) {
        //System.out.println("handle(), topic = " + topic + ", handler = " + handler + ", message = " + message);
        try {
            handler.handle(message);
        } catch (Throwable e) {
            log.log(Level.WARNING, "Failed to handle on topic: " + topic, e);
            publishLocal(Bus.ON_ERROR, Json.createObject().set("topic", topic).set("message", message).set("cause", e));
        }
    }

    private void scheduleHandle(String topic, Handler<Message> handler, Message message) {
        //System.out.println("scheduleHandle(), topic = " + topic + ", handler = " + handler + ", message = " + message);
        if (message.isLocal())
            handle(topic, handler, message);
        else
            Platform.scheduleDeferred(ignore -> SimpleBus.this.handle(topic, handler, message));
    }

    private Registration subscribeImpl(boolean local, String topic, Handler<? extends Message> handler) {
        doSubscribe(local, topic, handler);
        return () -> doUnsubscribe(local, topic, handler);
    }
}