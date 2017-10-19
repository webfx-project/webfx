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

import naga.platform.json.spi.JsonObject;
import naga.platform.bus.BusHook;
import naga.platform.bus.BusOptions;
import naga.platform.client.websocket.WebSocket;
import naga.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/ReconnectBus.java">Original Goodow class</a>
 */
public class ReconnectBus extends WebSocketBus {
    private static final String AUTO_RECONNECT = "reconnect";
    private final FuzzingBackOffGenerator backOffGenerator;
    private BusHook hook;
    private boolean reconnect;
    private final List<JsonObject> queuedMessages = new ArrayList<>();
    private final WebSocketBusOptions options;

    public ReconnectBus(BusOptions options) {
        this((WebSocketBusOptions) options);
    }

    private ReconnectBus(WebSocketBusOptions options) {
        super(options);
        this.options = options;
        JsonObject socketOptions = options.getSocketOptions();
        reconnect = socketOptions == null || !socketOptions.has(AUTO_RECONNECT) || socketOptions.getBoolean(AUTO_RECONNECT);
        backOffGenerator = new FuzzingBackOffGenerator(1000, 30 * 60 * 1000, 0.5);

        super.setHook(new BusHookProxy() {
            @Override
            public void handleOpened() {
                backOffGenerator.reset();

                for (Map.Entry<String, Integer> entry : handlerCount.entrySet()) {
                    String topic = entry.getKey();
                    //assert entry.getValue() > 0 : "Handlers registered on " + topic + " shouldn't be empty";
                    sendUnsubscribe(topic);
                    sendSubscribe(topic);
                }

                if (queuedMessages.size() > 0) {
                    List<JsonObject> copy = new ArrayList<>(queuedMessages);
                    queuedMessages.clear();
                    // Drain any messages that came in while the channel was not open.
                    for (JsonObject msg : copy) // copy.forEach does't compile with TeaVM
                        send(msg);
                }
                super.handleOpened();
            }

            @Override
            public void handlePostClose() {
                if (reconnect) {
                    Runnable runnable = () -> {
                        if (reconnect)
                            reconnect();
                    };
                    Scheduler.scheduleDelay(backOffGenerator.next().targetDelay, runnable);
                }
                super.handlePostClose();
            }

            @Override
            protected BusHook delegate() {
                return hook;
            }
        });
    }

    private void reconnect() {
        if (getReadyState() == WebSocket.State.OPEN || getReadyState() == WebSocket.State.CONNECTING)
            return;
        if (webSocket != null)
            webSocket.close();
        connect(serverUri, options);
    }

    @Override
    protected void doClose() {
        reconnect = false;
        backOffGenerator.reset();
        queuedMessages.clear();
        super.doClose();
    }

    @Override
    protected void send(JsonObject msg) {
        if (getReadyState() == WebSocket.State.OPEN) {
            super.send(msg);
            return;
        }
        if (reconnect)
            reconnect();
        String type = msg.getString(TYPE);
        if ("ping".equals(type) || "register".equals(type))
            return;
        queuedMessages.add(msg);
    }
}
