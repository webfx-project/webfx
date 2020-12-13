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

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Registration} that will call {@link Registration#unregister()} on
 * all added handlers if {@link Registration#unregister()} is called on this object.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/Registrations.java">Original Goodow class</a>
 */
public final class Registrations implements Registration {
    private List<Registration> registrations;

    public Registrations add(Registration registration) {
        //assert registration != null : "registration shouldn't be null";
        if (registrations == null)
            registrations = new ArrayList<>();
        registrations.add(registration);
        return this;
    }

    @Override
    public void unregister() {
        if (registrations != null) {
            for (Registration registration : registrations)
                registration.unregister();
            // make sure we remove the handlers to avoid potential leaks
            // if someone fails to null out their reference to us
            registrations.clear();
            registrations = null;
        }
    }

    public Registration wrap(Registration registration) {
        add(registration);
        return () -> {
            registrations.remove(registration);
            registration.unregister();
        };
    }
}
