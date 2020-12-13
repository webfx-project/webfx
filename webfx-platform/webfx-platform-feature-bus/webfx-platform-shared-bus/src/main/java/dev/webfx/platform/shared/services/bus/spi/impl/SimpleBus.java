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
package dev.webfx.platform.shared.services.bus.spi.impl;

import dev.webfx.platform.shared.services.bus.Bus;
import dev.webfx.platform.shared.services.bus.BusHook;
import dev.webfx.platform.shared.services.bus.Message;
import dev.webfx.platform.shared.services.bus.Registration;
import dev.webfx.platform.shared.services.json.Json;
import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.services.scheduler.Scheduler;
import dev.webfx.platform.shared.util.async.AsyncResult;
import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.util.async.Handler;

import java.util.*;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/SimpleBus.java">Original Goodow class</a>
 */
@SuppressWarnings("rawtypes")
public class SimpleBus implements Bus {

    private static final String ON_OPEN = "bus/onOpen";
    private static final String ON_CLOSE = "bus/onClose";
    private static final String ON_ERROR = "bus/onError";

    private final Map<String, List<Handler<Message>>> localHandlerMap = new HashMap<>();
    private final Map<String, List<Handler<Message>>> remoteHandlerMap = new HashMap<>();
    protected final Map<String, Handler<AsyncResult<Message>>> replyHandlers = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();
    protected BusHook hook;
    private boolean open;

    public SimpleBus() {
        this(true);
    }

    public SimpleBus(boolean alreadyOpen) {
        registerBusStateLocalHandlers();
        if (alreadyOpen)
            onOpen();
    }

    private void registerBusStateLocalHandlers() {
        subscribeLocal(ON_OPEN, msg -> onOpen());
        subscribeLocal(ON_CLOSE, msg -> onClose(msg.body()));
        subscribeLocal(ON_ERROR, msg -> onError(msg.body()));
    }

    protected void publishOnOpen() {
        publishLocal(ON_OPEN, null);
    }

    protected void publishOnClose(Object reason) {
        publishLocal(ON_CLOSE, reason);
    }

    protected void publishOnError(Object error) {
        publishLocal(ON_ERROR, error);
    }

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

    public Map<String, List<Handler<Message>>> getHandlerMap(boolean local) {
        return local ? localHandlerMap : remoteHandlerMap;
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
        publishLocal(ON_CLOSE, null);
    }

    protected void onOpen() {
        open = true;
        Logger.log("Bus open");
        if (hook != null)
            hook.handleOpened();
    }

    protected void onMessage(String address, String replyAddress, Object body) {
        SimpleMessage message = new SimpleMessage<>(false, false, this, address, replyAddress, body);
        internalHandleReceiveMessage(message);
    }

    protected void onClose(Object reason) {
        Logger.log("Bus closed, reason = " + reason);
        open = false;
        clearHandlers();
        if (hook != null)
            hook.handlePostClose();
    }

    protected void onError(Object reason) {
    }

    protected boolean doSubscribe(boolean local, String address, Handler<? extends Message> handler) {
        checkNotNull("address", address);
        checkNotNull("handler", handler);
        Map<String, List<Handler<Message>>> handlerMap = getHandlerMap(local);
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
        SimpleMessage message = new SimpleMessage(local, send, this, address, replyAddress, msg);
        if (!internalHandleReceiveMessage(message) && replyAddress != null)
            replyHandlers.remove(replyAddress);
    }

    protected <T> boolean doUnsubscribe(boolean local, String address, Handler<Message<T>> handler) {
        checkNotNull("address", address);
        checkNotNull("handler", handler);
        Map<String, List<Handler<Message>>> handlerMap = getHandlerMap(local);
        List<Handler<Message>> handlers = handlerMap.get(address);
        if (handlers == null)
            return false;
        boolean removed = handlers.remove(handler);
        if (handlers.isEmpty())
            handlerMap.remove(address);
        return removed;
    }

    void clearHandlers() {
        clearReplyHandlers();
        getHandlerMap(false).clear();
    }

    void clearReplyHandlers() {
        Iterator<Map.Entry<String, Handler<AsyncResult<Message>>>> it = replyHandlers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Handler<AsyncResult<Message>>> entry = it.next();
            if (shouldClearReplyHandlerNow(entry.getKey())) {
                entry.getValue().handle(Future.failedFuture(new Exception("Bus closed")));
                it.remove();
            }
        }
    }

    protected boolean shouldClearReplyHandlerNow(String replyAddress) {
        return true;
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

    protected String makeUUID() {
        return idGenerator.next(36);
    }

    private void doReceiveMessage(Message message) {
        String address = message.address();
        List<Handler<Message>> handlers = getHandlerMap(true).get(address);
        if (handlers == null)
            handlers = getHandlerMap(false).get(address);
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
            } else
                Logger.log("Unknown message address: " + address);
        }
    }

    private void handle(String address, Handler<AsyncResult<Message>> handler, Message message) {
        //Logger.log("handle(), address = " + address + ", handler = " + handler + ", message = " + message);
        try {
            handler.handle(Future.succeededFuture(message));
        } catch (Throwable e) {
            Logger.log("Failed to handle on address: " + address, e);
            publishLocal(ON_ERROR, Json.createObject().set("address", address).set("message", message).set("cause", e));
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

    protected static void checkNotNull(String paramName, Object param) {
        if (param == null)
            throw new IllegalArgumentException("Parameter " + paramName + " must be specified");
    }

}