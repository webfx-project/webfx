package naga.providers.platform.server.vertx.bus;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import naga.platform.bus.*;
import naga.platform.json.Json;
import naga.platform.json.listmap.MapBasedJsonObject;
import naga.util.async.AsyncResult;
import naga.util.async.Future;
import naga.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public final class VertxBus implements Bus {

    private final EventBus eventBus;
    private BusOptions options;

    public VertxBus(EventBus eventBus, BusOptions options) {
        this.eventBus = eventBus;
        this.options = options;
    }

    @Override
    public void close() {
        eventBus.close(event -> {});
    }

    @Override
    public Bus publish(String address, Object msg) {
        eventBus.publish(address, genericToVertxObject(msg));
        return this;
    }

    @Override
    public Bus publishLocal(String address, Object msg) {
        eventBus.publish(address, genericToVertxObject(msg));
        return this;
    }

    @Override
    public <T> Bus send(String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        eventBus.<T>send(address, genericToVertxObject(msg), ar -> replyHandler.handle(vertxToGenericMessageAsyncResult(ar, false)));
        return this;
    }

    @Override
    public <T> Bus sendLocal(String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        eventBus.<T>send(address, genericToVertxObject(msg), ar -> replyHandler.handle(vertxToGenericMessageAsyncResult(ar, true)));
        return this;
    }

    @Override
    public Bus setHook(BusHook hook) {
        throw new UnsupportedOperationException(); // not yet implemented
    }

    @Override
    public <T> Registration subscribe(String address, Handler<Message<T>> handler) {
        MessageConsumer<T> consumer = eventBus.consumer(address);
        consumer.handler(message -> handler.handle(vertxToGenericMessage(message, false)));
        return consumer::unregister;
    }

    @Override
    public <T> Registration subscribeLocal(String address, Handler<Message<T>> handler) {
        MessageConsumer<T> consumer = eventBus.consumer(address);
        consumer.handler(message -> handler.handle(vertxToGenericMessage(message, true)));
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
        return new Message<T>() {
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
                vertxMessage.<T1>reply(genericToVertxObject(msg), ar -> replyHandler.handle(vertxToGenericMessageAsyncResult(ar, false)));
            }

            @Override
            public String replyTopic() {
                return vertxMessage.replyAddress();
            }

            @Override
            public String topic() {
                return vertxMessage.address();
            }
        };
    }
}
