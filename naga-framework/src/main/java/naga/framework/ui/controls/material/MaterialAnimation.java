package naga.framework.ui.controls.material;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;
import naga.fx.properties.Properties;
import naga.fx.properties.Unregistrable;
import naga.util.collection.Collections;
import naga.util.function.Consumer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class MaterialAnimation {

    private final static Duration MATERIAL_ANIMATION_DURATION = Duration.millis(400);

    private Timeline animation;
    private Collection<KeyValue> animationKeyValues = new ArrayList<>();
    private Runnable pendingPlay;

    public Unregistrable runNowAndOnPropertiesChange(Consumer<ObservableValue> runnable, ObservableValue... properties) {
        return Properties.runNowAndOnPropertiesChange(p -> {
            runnable.accept(p);
            if (animationKeyValues != null && pendingPlay == null)
                Platform.runLater(pendingPlay = this::play);
        }, properties);
    }

    public <T> void playEaseOut(WritableValue<T> target, T endValue) {
        play(new KeyValue(target, endValue, Properties.EASE_OUT_INTERPOLATOR));
    }

    public void play(KeyValue... keyValues) {
        if (keyValues.length > 0) {
            if (animationKeyValues == null)
                animationKeyValues = new ArrayList<>();
            java.util.Collections.addAll(animationKeyValues, keyValues);
        }
    }

    private void play() {
        if (animationKeyValues != null) {
            if (animation != null)
                animation.stop();
            animation = new Timeline(new KeyFrame(MATERIAL_ANIMATION_DURATION, Collections.toArray(animationKeyValues, KeyValue[]::new)));
            animation.play();
            animationKeyValues = null;
            pendingPlay = null;
        }
    }

}
