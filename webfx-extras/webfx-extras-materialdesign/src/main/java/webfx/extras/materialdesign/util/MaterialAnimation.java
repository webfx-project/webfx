package webfx.extras.materialdesign.util;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;
import webfx.kit.util.properties.Properties;
import webfx.kit.util.properties.Unregisterable;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class MaterialAnimation {

    // Ease out interpolator closer to the web standard than the one proposed in JavaFx (ie Interpolator.EASE_OUT)
    private final static Interpolator EASE_OUT_INTERPOLATOR = Interpolator.SPLINE(0, .75, .25, 1);
    private final static Duration MATERIAL_ANIMATION_DURATION = Duration.millis(400);

    private Timeline timeline;
    private Collection<KeyValue> keyValues = new ArrayList<>();
    private Runnable onFinished;
    private Runnable pendingPlay;

    public Unregisterable runNowAndOnPropertiesChange(Runnable runnable, ObservableValue... properties) {
        return Properties.runNowAndOnPropertiesChange(() -> {
            keyValues = null;
            runnable.run();
            play();
        }, properties);
    }

    public <T> MaterialAnimation addEaseOut(WritableValue<T> target, T endValue) {
        return add(target, endValue, EASE_OUT_INTERPOLATOR);
    }

    public <T> MaterialAnimation add(WritableValue<T> target, T endValue, Interpolator interpolator) {
        if (!Objects.equals(target.getValue(), endValue))
            add(new KeyValue(target, endValue, interpolator));
        return this;
    }

    public MaterialAnimation add(KeyValue... keyValues) {
        if (keyValues.length > 0) {
            if (this.keyValues == null)
                this.keyValues = new ArrayList<>();
            java.util.Collections.addAll(this.keyValues, keyValues);
        }
        return this;
    }

    public MaterialAnimation setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
        return this;
    }

    public void play() {
        play(true);
    }

    public void play(boolean animate) {
        if (timeline != null) {
            timeline.jumpTo(timeline.getTotalDuration());
            timeline.stop();
            timeline = null;
        }
        if (!animate) {
            if (keyValues != null)
                for (KeyValue keyValue : keyValues) {
                    WritableValue target = keyValue.getTarget();
                    target.setValue(keyValue.getEndValue());
                }
            keyValues = null;
            runOnFinished();
        } else if (pendingPlay == null)
            Platform.runLater(pendingPlay = this::playNow);
    }

    private void runOnFinished() {
        if (onFinished != null)
            onFinished.run();
        onFinished = null;
    }

    private void playNow() {
        if (keyValues != null) {
            timeline = new Timeline(new KeyFrame(MATERIAL_ANIMATION_DURATION, Collections.toArray(keyValues, KeyValue[]::new)));
            Runnable onFinished = this.onFinished;
            if (onFinished != null)
                timeline.setOnFinished(e -> onFinished.run());
            this.onFinished = null;
            timeline.play();
            keyValues = null;
        } else
            runOnFinished();
        pendingPlay = null;
    }

}
