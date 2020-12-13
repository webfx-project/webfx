package dev.webfx.platform.vertx.services.bus.spi.impl;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import dev.webfx.platform.shared.services.bus.*;
import dev.webfx.platform.shared.services.json.Json;
import dev.webfx.platform.shared.services.json.spi.impl.listmap.MapBasedJsonObject;
import dev.webfx.platform.shared.util.async.AsyncResult;
import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.util.async.Handler;

/**
 * @author Bruno Salmon
 */
final class VertxBus implements Bus {

    private final EventBus eventBus;
    private final BusOptions options;
    private boolean open;

    VertxBus(EventBus eventBus, BusOptions options) {
        this.eventBus = eventBus;
        this.options = options;
    }

    @Override
    public void close() {
        eventBus.close(event -> open = false);
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public Bus publish(boolean local, String address, Object msg) {
        eventBus.publish(address, genericToVertxObject(msg));
        return this;
    }

    public <T> Bus send(boolean local, String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        eventBus.<T>request(address, genericToVertxObject(msg), ar -> replyHandler.handle(vertxToGenericMessageAsyncResult(ar, local)));
        return this;
    }

    @Override
    public Bus setHook(BusHook hook) {
        throw new UnsupportedOperationException(); // not yet implemented
    }

    public <T> Registration subscribe(boolean local, String address, Handler<Message<T>> handler) {
        MessageConsumer<T> consumer = eventBus.consumer(address);
        consumer.handler(message -> handler.handle(vertxToGenericMessage(message, local)));
        return consumer::unregister;
    }

    private static Object genericToVertxObject(Object object) {
        if (object instanceof MapBasedJsonObject)
            object = ((MapBasedJsonObject) object).getNativeElement();
        return object;
    }

    private static Object vertxToGenericObject(Object vertxObject) {
        Object object = vertxObject;
        if (object instanceof JsonObject)
            object = Json.createObject(object);
        return object;
    }

    private static <T> AsyncResult<Message<T>> vertxToGenericMessageAsyncResult(io.vertx.core.AsyncResult<io.vertx.core.eventbus.Message<T>> ar, boolean local) {
        if (ar.failed())
            return Future.failedFuture(ar.cause());
        return Future.succeededFuture(vertxToGenericMessage(ar.result(), local));
    }

    private static <T> Message<T> vertxToGenericMessage(io.vertx.core.eventbus.Message<T> vertxMessage, boolean local) {
        return new Message<>() {
            @Override
            public T body() {
                return (T) vertxToGenericObject(vertxMessage.body());
            }

            @Override
            public void fail(int failureCode, String msg) {
                vertxMessage.fail(failureCode, msg);
            }

            @Override
            public boolean isLocal() {
                return local;
            }

            @Override
            public void reply(Object msg) {
                vertxMessage.reply(genericToVertxObject(msg));
            }

            @Override
            public <T1> void reply(Object msg, Handler<AsyncResult<Message<T1>>> replyHandler) {
                vertxMessage.<T1>replyAndRequest(genericToVertxObject(msg), ar -> replyHandler.handle(vertxToGenericMessageAsyncResult(ar, false)));
            }

            @Override
            public String replyAddress() {
                return vertxMessage.replyAddress();
            }

            @Override
            public String address() {
                return vertxMessage.address();
            }
        };
    }
}
