package javafx.scene.media;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

/**
 * @author Bruno Salmon
 */
public final class Media {

    private final String source;

    private ObjectProperty<Duration> durationProperty;

    public Media(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }


    public ReadOnlyObjectProperty<Duration> durationProperty() {
        if (durationProperty == null)
             durationProperty = new SimpleObjectProperty<>(Duration.UNKNOWN);
        return durationProperty;
    }

    public Duration getDuration() {
        return durationProperty().get();
    }

    // For WebFX internal usage only
    public void setDuration(Duration duration) {
        durationProperty(); // Ensuring non-nullity
        durationProperty.set(duration);
    }
}
