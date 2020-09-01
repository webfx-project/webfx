/*
 * Copyright (c) 2018 by Gerrit Grunwald
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

package eu.hansolo.fx.odometer;

import eu.hansolo.fx.odometer.event.OdometerObserver;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.HashMap;


public class OdometerBuilder<B extends OdometerBuilder<B>> {
    private HashMap<String, Property> properties = new HashMap<>();


    // ******************** Constructors **************************************
    protected OdometerBuilder() {}


    // ******************** Methods *******************************************
    public static final OdometerBuilder create() {
        return new OdometerBuilder();
    }

    public final B value(final double VALUE) {
        properties.put("value", new SimpleDoubleProperty(VALUE));
        return (B)this;
    }

    public final B digits(final int DIGITS) {
        properties.put("digits", new SimpleIntegerProperty(DIGITS));
        return (B)this;
    }

    public final B decimals(final int DECIMALS) {
        properties.put("decimals", new SimpleIntegerProperty(DECIMALS));
        return (B)this;
    }

    public final B digitBackgroundColor(final Color COLOR) {
        properties.put("digitBackgroundColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B digitForegroundColor(final Color COLOR) {
        properties.put("digitForegroundColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B decimalBackgroundColor(final Color COLOR) {
        properties.put("decimalBackgroundColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B decimalForegroundColor(final Color COLOR) {
        properties.put("decimalForegroundColor", new SimpleObjectProperty<>(COLOR));
        return (B)this;
    }

    public final B onZeroCrossing(final OdometerObserver OBSERVER) {
        properties.put("onZeroCrossing", new SimpleObjectProperty<>(OBSERVER));
        return (B)this;
    }

    public final Odometer build() {
        final Odometer CONTROL = new Odometer();
        for (String key : properties.keySet()) {
            if ("value".equals(key)) {
                CONTROL.setValue(((DoubleProperty) properties.get(key)).get());
            } else if ("digits".equals(key)) {
                CONTROL.setDigits(((IntegerProperty) properties.get(key)).get());
            } else if ("decimals".equals(key)) {
                CONTROL.setDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("digitBackgroundColor".equals(key)) {
                CONTROL.setDigitBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("digitForegroundColor".equals(key)) {
                CONTROL.setDigitForegroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("decimalBackgroundColor".equals(key)) {
                CONTROL.setDecimalBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("decimalForegroundColor".equals(key)) {
                CONTROL.setDecimalForegroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("onZeroCrossing".equals(key)) {
                CONTROL.setOnDigitChanged(((ObjectProperty<OdometerObserver>) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
