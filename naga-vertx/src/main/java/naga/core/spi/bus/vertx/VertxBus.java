package naga.core.spi.bus.vertx;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import naga.core.spi.bus.*;
import naga.core.spi.plat.WebSocket;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class VertxBus implements Bus {

    private final EventBus eventBus;
    private WebSocket.State state = WebSocket.State.OPEN;
    private BusOptions options;

    public VertxBus(EventBus eventBus, BusOptions options) {
        this.eventBus = eventBus;
        this.options = options;
    }

    @Override
    public void close() {
        state = WebSocket.State.CLOSING;
        eventBus.close(event -> state = WebSocket.State.CLOSED);
    }

    @Override
    public WebSocket.State getReadyState() {
        return state;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public Bus publish(String topic, Object msg) {
        eventBus.publish(topic, msg);
        return this;
    }

    @Override
    public Bus publishLocal(String topic, Object msg) {
        eventBus.publish(topic, msg);
        return this;
    }

    @Override
    public <T> Bus send(String topic, Object msg, Handler<Message<T>> replyHandler) {
        eventBus.send(topic, msg, asyncResult -> replyHandler.handle(fromVertxMessage(asyncResult.result(), false)));
        return this;
    }

    @Override
    public <T> Bus sendLocal(String topic, Object msg, Handler<Message<T>> replyHandler) {
        eventBus.send(topic, msg, asyncResult -> replyHandler.handle(fromVertxMessage(asyncResult.result(), true)));
        return this;
    }

    @Override
    public Bus setHook(BusHook hook) {
        throw new UnsupportedOperationException(); // not yet implemented
    }

    @Override
    public <T> Registration subscribe(String topic, Handler<Message<T>> handler) {
        MessageConsumer<T> consumer = eventBus.consumer(topic);
        consumer.handler(message -> handler.handle(toVertxMessage(message, false)));
        return consumer::unregister;
    }

    @Override
    public <T> Registration subscribeLocal(String topic, Handler<Message<T>> handler) {
        MessageConsumer<T> consumer = eventBus.consumer(topic);
        consumer.handler(message -> handler.handle(toVertxMessage(message, true)));
        return consumer::unregister;
    }


    private static <T> Message<T> toVertxMessage(io.vertx.core.eventbus.Message<T> message, boolean local) {
        return new Message<T>() {
            @Override
            public T body() {
                return (T) message.body();
            }

            @Override
            public void fail(int failureCode, String msg) {
                message.fail(failureCode, msg);
            }

            @Override
            public boolean isLocal() {
                return local;
            }

            @Override
            public void reply(Object msg) {
                message.reply(msg);
            }

            @Override
            public <T1> void reply(Object msg, Handler<Message<T1>> replyHandler) {
                message.reply(msg, asyncResult -> replyHandler.handle(fromVertxMessage(asyncResult.result(), false)));
            }

            @Override
            public String replyTopic() {
                return message.replyAddress();
            }

            @Override
            public String topic() {
                return message.address();
            }
        };
    }

    private static <T> Message<T> fromVertxMessage(io.vertx.core.eventbus.Message<Object> message, boolean local) {
        return new Message<T>() {
            @Override
            public T body() {
                return (T) message.body();
            }

            @Override
            public void fail(int failureCode, String msg) {
                message.fail(failureCode, msg);
            }

            @Override
            public boolean isLocal() {
                return local;
            }

            @Override
            public void reply(Object msg) {
                message.reply(msg);
            }

            @Override
            public <T> void reply(Object msg, Handler<Message<T>> replyHandler) {
                message.reply(msg, asyncResult -> replyHandler.handle(fromVertxMessage(asyncResult.result(), local)));
            }

            @Override
            public String replyTopic() {
                return message.replyAddress();
            }

            @Override
            public String topic() {
                return message.address();
            }
        };
    }

}
