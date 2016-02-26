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
package naga.core.spi.platform.client.android;

import naga.core.spi.bus.BusOptions;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.javaplat.JavaClientPlatform;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/android/AndroidPlatform.java">Original Goodow class</a>
 */
public final class AndroidPlatform extends JavaClientPlatform {

    /**
     * Providing AndroidPlatform.register() method if needed but this explicit call is normally not necessary
     * as this platform is listed in META-INF/services and can therefore be found by the ServiceLoader.
     * However there is currently an issue with the ServiceLoader on Android because META-INF/services is excluded from
     * the final application archive (apk file). So the explicit call is finally required until this issue is fixed.
     */

    public static void register() {
        Platform.register(new AndroidPlatform());
    }

    private AndroidPlatform() {
        super(new AndroidScheduler());
    }

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        super.setPlatformBusOptions(options);
        // Changing localhost to 10.0.2.2 which is the way to access localhost from the android simulator
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        if ("localhost".equals(socketBusOptions.getServerHost()))
            socketBusOptions.setServerHost("10.0.2.2");
    }
}
