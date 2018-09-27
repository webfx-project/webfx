package mongooses.core.backend.activities.monitor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxkits.extra.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
final class MonitorPresentationModel {

    private final Property<DisplayResult> memoryDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> memoryDisplayResultProperty() { return memoryDisplayResultProperty; }

    private final Property<DisplayResult> cpuDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> cpuDisplayResultProperty() { return cpuDisplayResultProperty; }
}
