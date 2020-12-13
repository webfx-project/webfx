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
package dev.webfx.platform.shared.services.bus;

/**
 * Registration objects returned when an event handler is bound (e.g. via
 * {@link Bus#subscribe}), used to deregister.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/PlatformFactory.java">Original Goodow class</a>
 */

public interface Registration {

    Registration EMPTY = () -> {
    };

    /**
     * Deregisters the handler associated with this registration object if the handler is still
     * attached to the event bus. If the handler is no longer attached to the event bus, this is a
     * no-op.
     */
    void unregister();
}
