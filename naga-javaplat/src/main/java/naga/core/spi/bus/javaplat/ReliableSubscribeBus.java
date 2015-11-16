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
package naga.core.spi.bus.javaplat;

import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusHook;
import naga.core.spi.bus.Message;
import naga.core.util.async.Handler;
import naga.core.spi.plat.Platform;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Converts a stream of possibly-missing, possibly-unordered, possibly-duplicated messages into a
 * stream of in-order, consecutive, no-dup messages.
 *
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *         <p>
 *         <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/ReliableSubscribeBus.java">Original Goodow class</a>
 */
public class ReliableSubscribeBus extends BusProxy {
    public static final String SEQUENCE_NUMBER = "sequence_number_key";
    public static final String PUBLISH_CHANNEL = "publish_channel";
    public static final String ACKNOWLEDGE_DELAY_MILLIS = "acknowledgeDelayMillis";

    private static final Logger log = Logger.getLogger(ReliableSubscribeBus.class.getName());
    private final String sequenceNumberKey;
    private final String publishChannel;
    /**
     * Delay acknowledgment in case we receive operations in the meantime.
     */
    private final int acknowledgeDelayMillis;
    private final JsonObject pendings; // {topic: {sequence: Message<?>}}
    private final JsonObject currentSequences;
    private final JsonObject knownHeadSequences;
    private final JsonObject acknowledgeScheduled;
    private final JsonObject acknowledgeNumbers;

    public ReliableSubscribeBus(Bus delegate, JsonObject options) {
        super(delegate);
        sequenceNumberKey =
                options == null || !options.has(SEQUENCE_NUMBER) ? "v" : options.getString(SEQUENCE_NUMBER);
        publishChannel =
                options == null || !options.has(PUBLISH_CHANNEL) ? "realtime/store" : options
                        .getString(PUBLISH_CHANNEL);
        acknowledgeDelayMillis =
                options == null || !options.has(ACKNOWLEDGE_DELAY_MILLIS) ? 3 * 1000 : (int) options
                        .getNumber(ACKNOWLEDGE_DELAY_MILLIS);
        pendings = Json.createObject();
        currentSequences = Json.createObject();
        knownHeadSequences = Json.createObject();
        acknowledgeScheduled = Json.createObject();
        acknowledgeNumbers = Json.createObject();

        delegate.setHook(new BusHookProxy() {
            @Override
            public boolean handleReceiveMessage(Message<?> message) {
                if (hook != null && !hook.handleReceiveMessage(message)) {
                    return false;
                }
                return onReceiveMessage(message);
            }

            @Override
            public boolean handleUnsubscribe(String topic) {
                if (needProcess(topic)) {
                    pendings.remove(topic);
                    currentSequences.remove(topic);
                    knownHeadSequences.remove(topic);
                    acknowledgeScheduled.remove(topic);
                    acknowledgeNumbers.remove(topic);
                }
                return super.handleUnsubscribe(topic);
            }

            @Override
            protected BusHook delegate() {
                return hook;
            }
        });
    }

    @Override
    public void close() {
        super.close();
        pendings.clear();
        currentSequences.clear();
        knownHeadSequences.clear();
        acknowledgeScheduled.clear();
        acknowledgeNumbers.clear();
    }

    public void synchronizeSequenceNumber(String topic, double initialSequenceNumber) {
        assert !currentSequences.has(topic) && !knownHeadSequences.has(topic)
                && !pendings.has(topic);
        initSequenceNumber(topic, initialSequenceNumber);
        // Send the first acknowledgment immediately, to quickly catch up any initial missing messages,
        // which might happen if the topic is currently active.
        catchup(topic, initialSequenceNumber);
    }

    protected void catchup(final String topic, double currentSequence) {
        String id = topic.substring(publishChannel.length() + 1);
        id = id.substring(0, id.lastIndexOf("/_watch"));
        delegate.send(publishChannel + "/_ops",
                Json.createObject().set("id", id).set("from", currentSequence + 1),
                new Handler<Message<JsonArray>>() {
                    @SuppressWarnings({"rawtypes", "unchecked"})
                    @Override
                    public void handle(Message<JsonArray> message) {
                        final String replyTopic = message.replyTopic();
                        message.body().forEach(new JsonArray.ListIterator() {
                            @Override
                            public void call(int index, Object value) {
                                onReceiveMessage(new MessageImpl(false, false, ReliableSubscribeBus.this,
                                        topic, replyTopic, value));
                            }
                        });
                    }
                });
    }

    protected double getSequenceNumber(String topic, Object body) {
        return ((JsonObject) body).getNumber(sequenceNumberKey);
    }

    protected boolean needProcess(String topic) {
        return topic.startsWith(publishChannel + "/") && topic.endsWith("/_watch") &&
                !topic.contains("/_presence/");
    }

    protected boolean onReceiveMessage(Message<?> message) {
        String topic = message.topic();
        Object body = message.body();
        if (!needProcess(topic)) {
            return true;
        }
        double sequence = getSequenceNumber(topic, body);
        if (!currentSequences.has(topic)) {
            initSequenceNumber(topic, sequence);
            return true;
        }

        double currentSequence = currentSequences.getNumber(topic);
        if (sequence <= currentSequence) {
            log.log(Level.CONFIG, "Old dup at sequence " + sequence + ", current is now "
                    + currentSequence);
            return false;
        }
        JsonObject pending = pendings.getObject(topic);
        Message<?> existing = pending.get("" + sequence);
        if (existing != null) {
            // Should not have pending data at a sequence we could have pushed out.
            assert sequence > currentSequence + 1 : "should not have pending data";
            log.log(Level.CONFIG, "Dup message: " + message);
            return false;
        }

        knownHeadSequences.set(topic, Math.max(knownHeadSequences.getNumber(topic), sequence));

        if (sequence > currentSequence + 1) {
            pending.set("" + sequence, message);
            log.log(Level.CONFIG, "Missed message, current sequence=" + currentSequence
                    + " incoming sequence=" + sequence);
            scheduleAcknowledgment(topic);
            return false;
        }

        assert sequence == currentSequence + 1 : "other cases should have been caught";
        String next;
        JsonArray messages = Json.createArray();
        while (true) {
            messages.push(message);
            currentSequences.set(topic, ++currentSequence);
            next = currentSequence + 1 + "";
            message = pending.get(next);
            if (message != null) {
                pending.remove(next);
            } else {
                break;
            }
        }
        scheduleMessages(messages);
        assert !pending.has(next);
        return false;
    }

    private void initSequenceNumber(String topic, double initialSequenceNumber) {
        currentSequences.set(topic, initialSequenceNumber);
        knownHeadSequences.set(topic, initialSequenceNumber);
        pendings.set(topic, Json.createObject());
    }

    /**
     * Acknowledgment Number is the next sequence number that the receiver is expecting
     */
    private void scheduleAcknowledgment(final String topic) {
        if (!acknowledgeScheduled.has(topic)) {
            acknowledgeScheduled.set(topic, true);
            Platform.scheduler().scheduleDelay(acknowledgeDelayMillis, new Handler<Void>() {
                @Override
                public void handle(Void event) {
                    if (acknowledgeScheduled.has(topic)) {
                        acknowledgeScheduled.remove(topic);
                        // Check we're still out of date, and not already catching up.
                        double knownHeadSequence = knownHeadSequences.getNumber(topic);
                        double currentSequence = currentSequences.getNumber(topic);
                        if (knownHeadSequence > currentSequence
                                && (!acknowledgeNumbers.has(topic) || knownHeadSequence > acknowledgeNumbers
                                .getNumber(topic))) {
                            acknowledgeNumbers.set(topic, knownHeadSequence);
                            log.log(Level.CONFIG, "Catching up to " + knownHeadSequence);
                            catchup(topic, currentSequence);
                        } else {
                            log.log(Level.FINE, "No need to catchup");
                        }
                    }
                }
            });
        }
    }

    private void scheduleMessages(final JsonArray messages) {
        Platform.scheduler().scheduleDeferred(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                messages.forEach(new JsonArray.ListIterator<Message<?>>() {
                    @Override
                    public void call(int index, Message<?> message) {
                        delegate.publishLocal(message.topic(), message.body());
                    }
                });
            }
        });
    }
}
