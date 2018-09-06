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
package webfx.platforms.java.client;

import webfx.platforms.core.client.url.location.WindowLocation;
import webfx.platforms.core.client.url.location.impl.WindowLocationImpl;
import webfx.platforms.core.services.bus.client.WebSocketBusOptions;
import webfx.platforms.core.services.bus.spi.BusService;
import webfx.platforms.core.spi.client.ClientPlatform;
import webfx.platforms.java.JavaPlatform;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public abstract class JavaClientPlatform extends JavaPlatform implements ClientPlatform {
/*

    @Override
    public BusFactory busFactory() { // ClientPlatform default method doesn't work while extending JavaPlatform
        return ReconnectBus::new; // So repeating it again...
    }

    @Override
    public BusOptions createBusOptions() { // ClientPlatform default method doesn't work while extending JavaPlatform
        return new WebSocketBusOptions(); // So repeating it again...
    }

    public void setPlatformBusOptions(BusOptions options) {
        super.setPlatformBusOptions(options);
        String json = ResourceService.getText("webfx/platforms/core/services/bus/client/conf/BusOptions.json").result();
        if (json != null)
            options.applyJson(Json.parseObject(json));
    }

*/
    @Override
    public WindowLocation getCurrentLocation() {
        WebSocketBusOptions busOptions = (WebSocketBusOptions) BusService.getBusOptions();
        return new WindowLocationImpl(busOptions.isServerSSL() ? "https" : "http", busOptions.getServerHost(), null, getBrowserHistory().getCurrentLocation());
    }
}