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
package dev.webfx.platform.shared.services.bus;

import dev.webfx.platform.shared.util.async.Handler;
import dev.webfx.platform.shared.util.async.AsyncResult;

/**
 * Represents a message on the event bus.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/Message.java">Original Goodow class</a>
 */

public interface Message<T> {
    /**
     * The body of the message
     */
    T body();

    /**
     * Signal that processing of this message failed. If the message was sent specifying a result
     * handler the handler will be called with a failure corresponding to the failure code and message
     * specified here
     *
     * @param failureCode A failure code to pass back to the sender
     * @param msg         A message to pass back to the sender
     */
    void fail(int failureCode, String msg);

    /**
     * @return Whether this message originated in the local session.
     */
    boolean isLocal();

    /**
     * Reply to this message. If the message was sent specifying a reply handler, that handler will be
     * called when it has received a reply. If the message wasn't sent specifying a receipt handler
     * this method does nothing.
     */
    void reply(Object msg);

    /**
     * The same as {@code reply(Object msg)} but you can specify handler for the reply - i.e. to
     * receive the reply to the reply.
     */
    @SuppressWarnings("hiding")
    <T> void reply(Object msg, Handler<AsyncResult<Message<T>>> replyHandler);

    /**
     * The reply address (if any)
     */
    String replyAddress();

    /**
     * The address the message was sent to
     */
    String address();
}
