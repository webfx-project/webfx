package naga.framework.ui.anim;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;
import naga.util.Objects;

/**
 * @author Bruno Salmon
 */
public class Animations {

    // Better ease out interpolator than the one proposed in JavaFx (ie Interpolator.EASE_OUT)
    public final static Interpolator EASE_OUT_INTERPOLATOR = Interpolator.SPLINE(0, .75, .25, 1);

    public static <T> void animateProperty(WritableValue<T> target, T finalValue) {
        animateProperty(target, finalValue, true);
    }

    public static <T> void animateProperty(WritableValue<T> target, T finalValue, boolean animate) {
        animateProperty(target, finalValue, animate ? EASE_OUT_INTERPOLATOR : null);
    }

    public static <T> void animateProperty(WritableValue<T> target, T finalValue, Interpolator interpolator) {
        if (!Objects.areEquals(target.getValue(), finalValue)) {
            if (interpolator == null)
                target.setValue(finalValue);
            else
                new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(target, finalValue, interpolator))).play();
        }
    }
}
