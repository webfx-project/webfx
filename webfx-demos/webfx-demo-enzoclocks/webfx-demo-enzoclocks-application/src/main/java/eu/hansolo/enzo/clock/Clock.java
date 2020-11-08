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

package eu.hansolo.enzo.clock;

import eu.hansolo.enzo.clock.skin.ClockSkin;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.scheduler.Scheduled;

import java.time.Duration;
import java.time.LocalTime;


/**
 * User: hansolo
 * Date: 31.10.12
 * Time: 14:17
 */
public class Clock extends Control {
    public enum Design {
        IOS6,
        DB,
        BRAUN,
        BOSCH
    }

    private static final int                  SHORT_INTERVAL = 20;
    private static final int                  LONG_INTERVAL  = 1000;
    
    private Scheduled                         periodicTickTask;

    private StringProperty                    text;
    private BooleanProperty                   discreteSecond;
    private BooleanProperty                   secondPointerVisible;
    private BooleanProperty                   nightMode;
    private ObjectProperty<Design>            design;
    private BooleanProperty                   highlightVisible;
    private ObjectProperty<LocalTime>         time;
    private BooleanProperty                   running;
    private boolean                           autoNightMode;
    private int                               updateInterval;


    // ******************** Constructors **************************************
    public Clock() {
        this(LocalTime.now());
    }
    public Clock(final LocalTime TIME) {
        getStyleClass().add("clock");
        text                 = new SimpleStringProperty(Clock.this, "text", "");
        discreteSecond       = new BooleanPropertyBase(false) {
            @Override public void set(final boolean DISCRETE_SECOND) {
                super.set(DISCRETE_SECOND);                
                updateInterval = DISCRETE_SECOND ? LONG_INTERVAL : SHORT_INTERVAL;                
            }
            @Override public Object getBean() { return Clock.this; }
            @Override public String getName() { return "discreteSecond"; }
        };        
        secondPointerVisible = new SimpleBooleanProperty(Clock.this, "secondPointerVisible", false);
        nightMode            = new SimpleBooleanProperty(Clock.this, "nightMode", false);
        design               = new ObjectPropertyBase<Design>(Design.IOS6) {
            @Override public void set(final Design DESIGN) {
                switch (DESIGN) {
                    case IOS6 : setDiscreteSecond(false); setSecondPointerVisible(true); break;
                    case DB   : setDiscreteSecond(false); setSecondPointerVisible(true); break;
                    case BRAUN: setDiscreteSecond(true); setSecondPointerVisible(true); break;
                    case BOSCH: setDiscreteSecond(true); setSecondPointerVisible(false); break;
                }
                super.set(DESIGN);                
            }
            @Override public Object getBean() { return Clock.this; }
            @Override public String getName() { return "design"; }
        };
        highlightVisible     = new SimpleBooleanProperty(Clock.this, "highlightVisible", false);
        time                 = new SimpleObjectProperty<>(Clock.this, "time", TIME);
        running              = new BooleanPropertyBase(false) {
            @Override public void set(final boolean RUNNING) { 
                super.set(RUNNING); 
                if (RUNNING) { scheduleTickTask(); } else { stopTask(periodicTickTask); }
            }
            @Override public Object getBean() { return Clock.this; }
            @Override public String getName() { return "running"; }
        };        
        updateInterval       = SHORT_INTERVAL;        
    }


    // ******************** Methods *******************************************
    public final String getText() { return text.get(); }
    public final void setText(final String TEXT) { text.set(TEXT); }
    public final StringProperty textProperty() { return text; }

    public final boolean isDiscreteSecond() { return discreteSecond.get(); }
    public final void setDiscreteSecond(final boolean DISCRETE_SECOND) { discreteSecond.set(DISCRETE_SECOND); }
    public final BooleanProperty discreteSecondProperty() { return discreteSecond; }

    public final boolean isSecondPointerVisible() { return secondPointerVisible.get(); }
    public final void setSecondPointerVisible(final boolean SECOND_POINTER_VISIBLE) { secondPointerVisible.set(SECOND_POINTER_VISIBLE); }
    public final BooleanProperty secondPointerVisibleProperty() { return secondPointerVisible; }

    public final boolean isNightMode() { return nightMode.get(); }
    public final void setNightMode(final boolean NIGHT_MODE) { nightMode.set(NIGHT_MODE); }
    public final BooleanProperty nightModeProperty() { return nightMode; }

    public final Design getDesign() { return design.get(); }
    public final void setDesign(final Design DESIGN) { design.set(DESIGN); }
    public final ObjectProperty<Design> designProperty() { return design; }

    public final boolean isHighlightVisible() { return highlightVisible.get(); }
    public final void setHighlightVisible(final boolean HIGHLIGHT_VISIBLE) { highlightVisible.set(HIGHLIGHT_VISIBLE); }
    public final BooleanProperty highlightVisibleProperty() { return highlightVisible; }

    public final LocalTime getTime() { return time.get(); }
    public final void setTime(final LocalTime TIME) { time.set(TIME); }
    public final ObjectProperty<LocalTime> timeProperty() { return time; }
    
    public final boolean isRunning() { return running.get(); }
    public final void setRunning(final boolean RUNNING) { running.set(RUNNING); }
    public final BooleanProperty runningProperty() { return running; }

    public final boolean isAutoNightMode() { return autoNightMode; }
    public final void setAutoNightMode(final boolean AUTO_NIGHT_MODE) { autoNightMode = AUTO_NIGHT_MODE; }

    private void tick() {        
        //Platform.runLater(() -> setTime(getTime().plus(Duration.ofMillis(updateInterval))));
        //setTime(getTime().plus(Duration.ofMillis(updateInterval)));

        setTime(LocalTime.now().plus(localTimeGap));
    }

    
    // ******************** Scheduled tasks ***********************************
    private Duration localTimeGap;

    private synchronized void scheduleTickTask() {
        //enableTickExecutorService();
        stopTask(periodicTickTask);
        //periodicTickTask = periodicTickExecutorService.scheduleAtFixedRate(() -> tick(), 0, updateInterval, TimeUnit.MILLISECONDS);
        localTimeGap = Duration.between(LocalTime.now(), getTime());
        periodicTickTask = UiScheduler.schedulePeriodic(updateInterval, this::tick);
    }

    private void stopTask(Scheduled task) {
        if (null == task) return;

        task.cancel();
        //task = null;
    }
    

    // ******************** Style related *************************************
    @Override protected Skin createDefaultSkin() {
        return new ClockSkin(this);
    }

/*
    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("clock.css").toExternalForm();
    }
*/
}
