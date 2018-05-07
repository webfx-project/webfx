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
package naga.platform.client.bus;

import naga.platform.json.Json;
import naga.platform.bus.Bus;
import naga.platform.bus.BusHook;
import naga.platform.bus.Message;
import naga.platform.bus.Registration;
import naga.platform.services.log.Logger;
import naga.scheduler.Scheduler;
import naga.util.async.AsyncResult;
import naga.util.async.Future;
import naga.util.async.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/SimpleBus.java">Original Goodow class</a>
 */
@SuppressWarnings("rawtypes")
public class SimpleClientBus implements Bus {

    static void checkNotNull(String paramName, Object param) {
        if (param == null)
            throw new IllegalArgumentException("Parameter " + paramName + " must be specified");
    }

    private Map<String, List<Handler<Message>>> handlerMap = new HashMap<>();
    final Map<String, Handler<AsyncResult<Message>>> replyHandlers = new HashMap<>();
    final IdGenerator idGenerator = new IdGenerator();
    BusHook hook;
    private boolean open = true;

    @Override
    public void close() {
        if (hook == null || hook.handlePreClose())
            doClose();
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public String getSessionId() {
        return "@";
    }

    @Override
    public Bus publish(boolean local, String address, Object msg) {
        return internalHandleSendOrPub(local, false, address, msg, null);
    }

    @Override
    public <T> Registration subscribe(boolean local, String address, Handler<Message<T>> handler) {
        return subscribeImpl(local, address, handler);
    }

    @Override
    public <T> Bus send(boolean local, String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        return internalHandleSendOrPub(local, true, address, msg, replyHandler);
    }

    @Override
    public Bus setHook(BusHook hook) {
        this.hook = hook;
        return this;
    }

    protected void doClose() {
        publishLocal(WebSocketBus.ON_CLOSE, null);
        clearHandlers();
        open = false;
        if (hook != null)
            hook.handlePostClose();
    }

    protected boolean doSubscribe(boolean local, String address, Handler<? extends Message> handler) {
        checkNotNull("address", address);
        checkNotNull("handler", handler);
        List<Handler<Message>> handlers = handlerMap.get(address);
        if (handlers != null && handlers.contains(handler))
            return false;
        if (handlers == null)
            handlerMap.put(address, handlers = new ArrayList<>());
        handlers.add((Handler) handler);
        return true;
    }

    @SuppressWarnings("unchecked")
    protected <T> void doSendOrPub(boolean local, boolean send, String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        checkNotNull("address", address);
        String replyAddress = null;
        if (replyHandler != null) {
            replyAddress = makeUUID();
            replyHandlers.put(replyAddress, (Handler) replyHandler);
        }
        ClientMessage message = new ClientMessage(local, send, this, address, replyAddress, msg);
        if (!internalHandleReceiveMessage(message) && replyAddress != null)
            replyHandlers.remove(replyAddress);
    }

    protected <T> boolean doUnsubscribe(boolean local, String address, Handler<Message<T>> handler) {
        checkNotNull("address", address);
        checkNotNull("handler", handler);
        List<Handler<Message>> handlers = handlerMap.get(address);
        if (handlers == null)
            return false;
        boolean removed = handlers.remove(handler);
        if (handlers.isEmpty())
            handlerMap.remove(address);
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

    <T> Bus internalHandleSendOrPub(boolean local, boolean send, String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        if (local || hook == null || hook.handleSendOrPub(send, address, msg, replyHandler))
            doSendOrPub(local, send, address, msg, replyHandler);
        return this;
    }

    String makeUUID() {
        return idGenerator.next(36);
    }

    private void doReceiveMessage(Message message) {
        String address = message.address();
        List<Handler<Message>> handlers = handlerMap.get(address);
        if (handlers != null) {
            // We make a copy since the handler might get unregistered from within the handler itself,
            // which would screw up our iteration
            List<Handler<Message>> copy = new ArrayList<>(handlers);
            // Drain any messages that came in while the channel was not open.
            for (Handler<Message> handler : copy)
                scheduleHandle(address, handler, message);
        } else {
            // Might be a reply message
            Handler<AsyncResult<Message>> handler = replyHandlers.get(address);
            if (handler != null) {
                replyHandlers.remove(address);
                scheduleHandleAsync(address, handler, message);
            }
        }
    }

    private void handle(String address, Handler<AsyncResult<Message>> handler, Message message) {
        //Logger.log("handle(), address = " + address + ", handler = " + handler + ", message = " + message);
        try {
            handler.handle(Future.succeededFuture(message));
        } catch (Throwable e) {
            Logger.log("Failed to handle on address: " + address, e);
            publishLocal(WebSocketBus.ON_ERROR, Json.createObject().set("address", address).set("message", message).set("cause", e));
        }
    }

    private void scheduleHandle(String address, Handler<Message> handler, Message message) {
        scheduleHandleAsync(address, ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
            else
                handler.handle(ar.result());
        }, message);
    }

    private void scheduleHandleAsync(String address, Handler<AsyncResult<Message>> handler, Message message) {
        //Logger.log("scheduleHandle(), address = " + address + ", handler = " + handler + ", message = " + message);
        if (message.isLocal())
            handle(address, handler, message);
        else
            Scheduler.scheduleDeferred(() -> handle(address, handler, message));
    }

    private <T> Registration subscribeImpl(boolean local, String address, Handler<Message<T>> handler) {
        doSubscribe(local, address, handler);
        return () -> doUnsubscribe(local, address, handler);
    }
}