/*
 * Note: this code is a fork of Goodow realtime-android project https://github.com/goodow/realtime-android
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
package naga.core.spi.platform.client.javaplat;

import naga.core.spi.bus.BusOptions;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.javaplat.jackson.JacksonJsonFactory;
import naga.core.spi.json.javaplat.smart.SmartJsonFactory;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.ClientPlatform;
import naga.core.spi.platform.client.WebSocketFactory;

import java.util.logging.Logger;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public abstract class JavaClientPlatform extends ClientPlatform {
    protected final JavaScheduler scheduler;
    protected final JsonFactory jsonFactory;
    protected final naga.core.spi.platform.client.WebSocketFactory webSocketFactory = new JavaWebSocketFactory();

    protected JavaClientPlatform() {
        this(new JavaScheduler());
    }

    protected JavaClientPlatform(JavaScheduler scheduler) {
        this.scheduler = scheduler;
        JsonFactory factory;
        try {
            // Using Jackson if provided (faster)
            factory =  new JacksonJsonFactory();
            factory.parse("{}"); // will throw a NoClassDefFoundError if Jackson is not provided
        } catch (NoClassDefFoundError e) {
            // Using Json-smart as second choice (much smaller)
            factory =  new SmartJsonFactory();
        }
        jsonFactory = factory;
    }

    @Override
    public Logger logger() {
        return Logger.getAnonymousLogger();
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public JsonFactory jsonFactory() {
        return jsonFactory;
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return webSocketFactory;
    }

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to Web Socket (unless already explicitly set by the application)
        if (socketBusOptions.getProtocol() == null)
            socketBusOptions.setProtocol(WebSocketBusOptions.Protocol.WS);
        socketBusOptions.turnUnsetPropertiesToDefault();
    }
}