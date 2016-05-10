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
package naga.core.spi.platform.client.java;

import naga.core.composite.CompositesFactory;
import naga.core.spi.bus.BusOptions;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.json.java.smart.SmartJsonObject;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.ClientPlatform;
import naga.core.spi.platform.client.ResourceService;
import naga.core.spi.platform.client.WebSocketFactory;
import naga.core.spi.platform.sql.jdbc.JdbcSqlService;
import naga.core.spi.sql.SqlService;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public abstract class JavaClientPlatform extends ClientPlatform {
    protected final JavaScheduler scheduler;
    protected final CompositesFactory jsonFactory = new SmartJsonObject();
    protected final WebSocketFactory webSocketFactory = new JavaWebSocketFactory();

    protected JavaClientPlatform() {
        this(new JavaScheduler());
    }

    protected JavaClientPlatform(JavaScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public CompositesFactory jsonFactory() {
        return jsonFactory;
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return webSocketFactory;
    }

    @Override
    public SqlService sqlService() {
        return JdbcSqlService.JDBC_SQL_SERVICE;
    }

    @Override
    public ResourceService resourceService() {
        return JavaResourceService.SINGLETON;
    }

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to Web Socket (unless already explicitly set by the application)
        if (socketBusOptions.getProtocol() == null)
            socketBusOptions.setProtocol(WebSocketBusOptions.Protocol.WS);
        super.setPlatformBusOptions(options);
    }
}