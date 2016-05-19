package naga.core.spi.platform.vertx;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import naga.core.json.Json;
import naga.core.json.listmap.MapBasedJsonObject;
import naga.core.bus.*;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
final class VertxBus implements Bus {

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
    public Bus publish(String topic, Object msg) {
        eventBus.publish(topic, toVertxObject(msg));
        return this;
    }

    @Override
    public Bus publishLocal(String topic, Object msg) {
        eventBus.publish(topic, toVertxObject(msg));
        return this;
    }

    @Override
    public <T> Bus send(String topic, Object msg, Handler<Message<T>> replyHandler) {
        eventBus.<T>send(topic, toVertxObject(msg), asyncResult -> replyHandler.handle(vertxToNagaMessage(asyncResult.result(), false)));
        return this;
    }

    @Override
    public <T> Bus sendLocal(String topic, Object msg, Handler<Message<T>> replyHandler) {
        eventBus.<T>send(topic, toVertxObject(msg), asyncResult -> replyHandler.handle(vertxToNagaMessage(asyncResult.result(), true)));
        return this;
    }

    @Override
    public Bus setHook(BusHook hook) {
        throw new UnsupportedOperationException(); // not yet implemented
    }

    @Override
    public <T> Registration subscribe(String topic, Handler<Message<T>> handler) {
        MessageConsumer<T> consumer = eventBus.consumer(topic);
        consumer.handler(message -> handler.handle(vertxToNagaMessage(message, false)));
        return consumer::unregister;
    }

    @Override
    public <T> Registration subscribeLocal(String topic, Handler<Message<T>> handler) {
        MessageConsumer<T> consumer = eventBus.consumer(topic);
        consumer.handler(message -> handler.handle(vertxToNagaMessage(message, true)));
        return consumer::unregister;
    }

    private static Object toVertxObject(Object object) {
        if (object instanceof MapBasedJsonObject)
            object = ((MapBasedJsonObject) object).getNativeElement();
        return object;
    }

    private static Object toNagaObject(Object vertxObject) {
        Object object = vertxObject;
        if (object instanceof JsonObject)
            object = Json.createObject(object);
        return object;
    }

    private static <T> Message<T> vertxToNagaMessage(io.vertx.core.eventbus.Message<T> vertxMessage, boolean local) {
        return new Message<T>() {
            @Override
            public T body() {
                return (T) toNagaObject(vertxMessage.body());
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
                vertxMessage.reply(toVertxObject(msg));
            }

            @Override
            public <T1> void reply(Object msg, Handler<Message<T1>> replyHandler) {
                vertxMessage.<T1>reply(toVertxObject(msg), asyncResult -> replyHandler.handle(vertxToNagaMessage(asyncResult.result(), false)));
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
