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
package dev.webfx.platform.client.services.websocketbus;


import dev.webfx.platform.shared.services.bus.Bus;
import dev.webfx.platform.shared.services.bus.BusHook;
import dev.webfx.platform.shared.util.async.AsyncResult;
import dev.webfx.platform.shared.util.async.Handler;
import dev.webfx.platform.shared.services.bus.Message;
import dev.webfx.platform.shared.services.bus.Registration;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
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
    public Bus publish(String address, Object msg) {
        return delegate.publish(address, msg);
    }

    @Override
    public Bus publishLocal(String address, Object msg) {
        return delegate.publishLocal(address, msg);
    }

    @Override
    public Bus publish(boolean local, String address, Object msg) {
        return delegate.publish(local, address, msg);
    }

    @Override
    public <T> Registration subscribe(String address, Handler<Message<T>> handler) {
        return delegate.subscribe(address, handler);
    }

    @Override
    public <T> Registration subscribeLocal(String address, Handler<Message<T>> handler) {
        return delegate.subscribeLocal(address, handler);
    }

    @Override
    public <T> Registration subscribe(boolean local, String address, Handler<Message<T>> handler) {
        return delegate.subscribe(local, address, handler);
    }

    @Override
    public <T> Bus send(String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        return delegate.send(address, msg, replyHandler);
    }

    @Override
    public <T> Bus sendLocal(String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        return delegate.sendLocal(address, msg, replyHandler);
    }

    @Override
    public <T> Bus send(boolean local, String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        return delegate.send(local, address, msg, replyHandler);
    }

    @Override
    public void close() {
        delegate.close();
    }
}