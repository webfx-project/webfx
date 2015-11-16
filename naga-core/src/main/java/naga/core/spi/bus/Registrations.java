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

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;

/**
 * A {@link Registration} that will call {@link Registration#unregister()} on
 * all added handlers if {@link Registration#unregister()} is called on this object.
 *
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *         <p>
 *         <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/Registrations.java">Original Goodow class</a>
 */
public class Registrations implements Registration {
    private JsonArray registrations;

    public Registrations add(Registration registration) {
        assert registration != null : "registration shouldn't be null";
        if (registrations == null) {
            registrations = Json.createArray();
        }
        registrations.push(registration);
        return this;
    }

    @Override
    public void unregister() {
        if (registrations != null) {
            registrations.forEach(new JsonArray.ListIterator<Registration>() {
                @Override
                public void call(int index, Registration value) {
                    value.unregister();
                }
            });
            // make sure we remove the handlers to avoid potential leaks
            // if someone fails to null out their reference to us
            registrations.clear();
            registrations = null;
        }
    }

    public Registration wrap(final Registration registration) {
        add(registration);
        return new Registration() {
            @Override
            public void unregister() {
                registrations.removeValue(registration);
                registration.unregister();
            }
        };
    }
}
