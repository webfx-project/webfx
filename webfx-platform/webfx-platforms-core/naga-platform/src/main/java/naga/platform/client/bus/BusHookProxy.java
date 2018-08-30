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
package naga.platform.client.bus;


import naga.platform.bus.BusHook;
import naga.platform.bus.Message;
import naga.util.async.AsyncResult;
import naga.util.async.Handler;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/BusHookProxy.java">Original Goodow class</a>
 */
public abstract class BusHookProxy implements BusHook {
    @Override
    public void handleOpened() {
        if (delegate() != null)
            delegate().handleOpened();
    }

    @Override
    public void handlePostClose() {
        if (delegate() != null)
            delegate().handlePostClose();
    }

    @Override
    public boolean handlePreClose() {
        return delegate() == null || delegate().handlePreClose();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean handlePreSubscribe(String address, Handler<? extends Message> handler) {
        return delegate() == null || delegate().handlePreSubscribe(address, handler);
    }

    @Override
    public boolean handleReceiveMessage(Message<?> message) {
        return delegate() == null || delegate().handleReceiveMessage(message);
    }

    @Override
    public <T> boolean handleSendOrPub(boolean send, String address, Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        return delegate() == null || delegate().handleSendOrPub(send, address, msg, replyHandler);
    }

    @Override
    public boolean handleUnsubscribe(String address) {
        return delegate() == null || delegate().handleUnsubscribe(address);
    }

    protected abstract BusHook delegate();
}