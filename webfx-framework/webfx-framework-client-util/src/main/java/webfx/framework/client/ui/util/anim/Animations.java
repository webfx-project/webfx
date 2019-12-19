package webfx.framework.client.ui.util.anim;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.util.Duration;
import webfx.platform.shared.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class Animations {

    // Ease out interpolator closer to the web standard than the one proposed in JavaFx (ie Interpolator.EASE_OUT)
    public final static Interpolator EASE_OUT_INTERPOLATOR = Interpolator.SPLINE(0, .75, .25, 1);

    public static <T> Timeline animateProperty(WritableValue<T> target, T finalValue) {
        return animateProperty(target, finalValue, true);
    }

    public static <T> Timeline animateProperty(WritableValue<T> target, T finalValue, boolean animate) {
        return animateProperty(target, finalValue, animate ? EASE_OUT_INTERPOLATOR : null);
    }

    public static <T> Timeline animateProperty(WritableValue<T> target, T finalValue, Interpolator interpolator) {
        if (!Objects.areEquals(target.getValue(), finalValue)) {
            if (interpolator == null)
                target.setValue(finalValue);
            else {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(target, finalValue, interpolator)));
                timeline.play();
                return timeline;
            }
        }
        return null;
    }

    public static void shake(Node node) {
        DoubleProperty x = node.layoutXProperty(); // translateX would be better but not yet emulated so using layoutX instead
        double xIni = x.getValue(), xMin = xIni - 10, xMax = xIni + 10;
        new Timeline(
                // Turning node to unmanaged (absolute positioning) to be sure layoutX will be considered
                new KeyFrame(Duration.millis(0),    new KeyValue(node.managedProperty(), false)),
                new KeyFrame(Duration.millis(100),  new KeyValue(x, xMin)),
                new KeyFrame(Duration.millis(200),  new KeyValue(x, xMax)),
                new KeyFrame(Duration.millis(300),  new KeyValue(x, xMin)),
                new KeyFrame(Duration.millis(400),  new KeyValue(x, xMax)),
                new KeyFrame(Duration.millis(500),  new KeyValue(x, xMin)),
                new KeyFrame(Duration.millis(600),  new KeyValue(x, xMax)),
                new KeyFrame(Duration.millis(700),  new KeyValue(x, xMin)),
                new KeyFrame(Duration.millis(800),  new KeyValue(x, xMax)),
                new KeyFrame(Duration.millis(900),  new KeyValue(x, xMin)),
                new KeyFrame(Duration.millis(1000), new KeyValue(x, xIni)),
                // Restoring the managed value
                new KeyFrame(Duration.millis(1000), new KeyValue(node.managedProperty(), node.isManaged()))
        ).play();
    }
}
