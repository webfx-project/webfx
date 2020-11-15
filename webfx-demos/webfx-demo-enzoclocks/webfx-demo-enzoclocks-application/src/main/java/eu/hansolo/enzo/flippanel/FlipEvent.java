/*
 * Copyright (c) 2015 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.enzo.flippanel;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;


/**
 * User: hansolo
 * Date: 16.04.14
 * Time: 22:07
 */
public class FlipEvent extends Event {
    public static final EventType<FlipEvent> FLIP_TO_FRONT_FINISHED = new EventType(ANY, "flipToFrontFinished");
    public static final EventType<FlipEvent> FLIP_TO_BACK_FINISHED  = new EventType(ANY, "flipToBackFinished");

    public FlipEvent(final Object SOURCE, final EventTarget TARGET, final EventType<FlipEvent> EVENT_TYPE) {
        super(SOURCE, TARGET, EVENT_TYPE);
    }
}
