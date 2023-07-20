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

    private final ObjectProperty<Duration> durationProperty = new SimpleObjectProperty<>(Duration.UNKNOWN);

    public Media(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public ReadOnlyObjectProperty<Duration> durationProperty() {
        return durationProperty;
    }

    public Duration getDuration() {
        return durationProperty.get();
    }

}
