/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
 */

/*
 * Copyright 2014 Goodow.com
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
package naga.commons.bus.websocket;


import naga.commons.bus.spi.Bus;
import naga.commons.bus.spi.BusHook;
import naga.commons.bus.spi.Message;
import naga.commons.bus.spi.Registration;
import naga.commons.util.async.Handler;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/BusProxy.java">Original Goodow class</a>
 */
public abstract class BusProxy implements Bus {
    protected final Bus delegate;
    protected BusHook hook;

    public BusProxy(Bus delegate) {
        this.delegate = delegate;
    }

    public Bus getDelegate() {
        return delegate;
    }

    @Override
    public Bus setHook(BusHook hook) {
        this.hook = hook;
        return this;
    }

    @Override
    public Bus publish(String topic, Object msg) {
        return delegate.publish(topic, msg);
    }

    @Override
    public Bus publishLocal(String topic, Object msg) {
        return delegate.publishLocal(topic, msg);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <T> Registration subscribe(String topic, Handler<Message<T>> handler) {
        return delegate.subscribe(topic, handler);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <T> Registration subscribeLocal(String topic, Handler<Message<T>> handler) {
        return delegate.subscribeLocal(topic, handler);
    }

    @Override
    public <T> Bus send(String topic, Object msg, Handler<Message<T>> replyHandler) {
        return delegate.send(topic, msg, replyHandler);
    }

    @Override
    public <T> Bus sendLocal(String topic, Object msg, Handler<Message<T>> replyHandler) {
        return delegate.sendLocal(topic, msg, replyHandler);
    }

    @Override
    public void close() {
        delegate.close();
    }
}