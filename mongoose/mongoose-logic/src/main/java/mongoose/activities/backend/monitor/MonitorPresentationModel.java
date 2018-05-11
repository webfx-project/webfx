package mongoose.activities.backend.monitor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.fxdata.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
class MonitorPresentationModel {

    private final Property<DisplayResult> memoryDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> memoryDisplayResultProperty() { return memoryDisplayResultProperty; }

    private final Property<DisplayResult> cpuDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> cpuDisplayResultProperty() { return cpuDisplayResultProperty; }
}
