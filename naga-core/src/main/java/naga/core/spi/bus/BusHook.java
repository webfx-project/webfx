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
package naga.core.spi.bus;

import naga.core.util.async.Handler;

/**
 * A hook that you can use to receive various events on the Bus.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *         <p>
 *         <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/BusHook.java">Original Goodow class</a>
 */
public interface BusHook {
    /**
     * Called when the bus is opened
     */
    void handleOpened();

    /**
     * Called when the bus is closed
     */
    void handlePostClose();

    /**
     * Called before close the bus
     *
     * @return true to close the bus, false to reject it
     */
    boolean handlePreClose();

    /**
     * Called before register a handler
     *
     * @param topic   The topic
     * @param handler The handler
     * @return true to let the registration occur, false otherwise
     */
    @SuppressWarnings("rawtypes")
    boolean handlePreSubscribe(String topic, Handler<? extends Message> handler);

    /**
     * Called when a message is received
     *
     * @param message The message
     * @return true To allow the message to deliver, false otherwise
     */
    boolean handleReceiveMessage(Message<?> message);

    /**
     * Called when sending or publishing on the bus
     *
     * @param send         if true it's a send else it's a publish
     * @param topic        The topic the message is being sent/published to
     * @param msg          The message
     * @param replyHandler Reply handler will be called when any reply from the recipient is received
     * @return true To allow the send/publish to occur, false otherwise
     */
    <T> boolean handleSendOrPub(boolean send, String topic, Object msg,
                                Handler<Message<T>> replyHandler);

    /**
     * Called when unregistering a handler
     *
     * @param topic The topic
     * @return true to let the unregistration occur, false otherwise
     */
    boolean handleUnsubscribe(String topic);
}
