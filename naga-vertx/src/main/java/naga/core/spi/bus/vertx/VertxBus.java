package naga.core.spi.bus.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.*;
import naga.core.spi.bus.*;
import naga.core.spi.bus.Message;
import naga.core.spi.bus.crossplat.MessageImpl;
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
        eventBus.send(topic, msg, toVertxHandler(replyHandler, false, topic));
        return this;
    }

    @Override
    public <T> Bus sendLocal(String topic, Object msg, Handler<Message<T>> replyHandler) {
        eventBus.send(topic, msg, toVertxHandler(replyHandler, true, topic));
        return this;
    }

    @Override
    public Bus setHook(BusHook hook) {
        throw new UnsupportedOperationException(); // not yet implemented
    }

    @Override
    public <T> Registration subscribe(String topic, Handler<Message<T>> handler) {
        MessageConsumer<Object> consumer = eventBus.consumer(topic);
        consumer.handler( event ->
                handler.handle(new Message<T>() {
                    @Override
                    public T body() {
                        return (T) event.body();
                    }

                    @Override
                    public void fail(int failureCode, String msg) {
                        event.fail(failureCode, msg);
                    }

                    @Override
                    public boolean isLocal() {
                        return false;
                    }

                    @Override
                    public void reply(Object msg) {
                        event.reply(msg);
                    }

                    @Override
                    public <T1> void reply(Object msg, Handler<Message<T1>> replyHandler) {
                        event.reply(msg, toVertxHandler(replyHandler, true, null));
                    }

                    @Override
                    public String replyTopic() {
                        return event.replyAddress();
                    }

                    @Override
                    public String topic() {
                        return event.address();
                    }
                }));
        return consumer::unregister;
    }

    @Override
    public <T> Registration subscribeLocal(String topic, Handler<Message<T>> handler) {
        return eventBus.localConsumer(topic)::unregister;
    }


    private <T> io.vertx.core.Handler<AsyncResult<io.vertx.core.eventbus.Message<T>>> toVertxHandler(Handler<Message<T>> handler, boolean local, String topic) {
        return handler == null ? null : (io.vertx.core.Handler<AsyncResult<io.vertx.core.eventbus.Message<T>>>) event ->
                handler.handle(new MessageImpl<>(local, true, VertxBus.this, topic, event.result().replyAddress(), event.result().body()));
    }

}
