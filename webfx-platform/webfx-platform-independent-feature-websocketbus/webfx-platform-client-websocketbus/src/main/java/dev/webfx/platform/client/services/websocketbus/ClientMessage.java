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
package dev.webfx.platform.client.services.websocketbus;

import dev.webfx.platform.shared.services.bus.Bus;
import dev.webfx.platform.shared.util.async.Handler;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.bus.Message;
import dev.webfx.platform.shared.util.async.AsyncResult;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/MessageImpl.java">Original Goodow class</a>
 */
final class ClientMessage<U> implements Message<U> {
    protected final U body;
    protected final Bus bus;
    protected final String address;
    protected final String replyAddress;
    protected final boolean send; // Is it a send or a publish?
    protected final boolean local;

    public ClientMessage(boolean local, boolean send, Bus bus, String address, String replyAddress, U body) {
        this.local = local;
        this.send = send;
        this.bus = bus;
        this.address = address;
        this.replyAddress = replyAddress;
        this.body = body;
    }

    @Override
    public String address() {
        return address;
    }

    @Override
    public U body() {
        return body;
    }

    @Override
    public void fail(int failureCode, String msg) {
        // sendReply(new ReplyException(ReplyFailure.RECIPIENT_FAILURE, failureCode, message), null);
    }

    @Override
    public boolean isLocal() {
        return local;
    }

    @Override
    public void reply(Object msg) {
        sendReply(msg, null);
    }

    @Override
    public <T> void reply(Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        sendReply(msg, replyHandler);
    }

    @Override
    public String replyAddress() {
        return replyAddress;
    }

    @Override
    public String toString() {
        return body == null ? "null" : body instanceof JsonObject ? ((JsonObject) body).toJsonString() : body.toString();
    }

    private <T> void sendReply(Object msg, Handler<AsyncResult<Message<T>>> replyHandler) {
        if (bus != null && replyAddress != null)
            bus.send(local, replyAddress, msg, replyHandler); // Send back reply
    }
}
