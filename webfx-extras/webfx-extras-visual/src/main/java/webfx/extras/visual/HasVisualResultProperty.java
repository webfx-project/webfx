package webfx.extras.visual;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */

public interface HasVisualResultProperty {

    Property<VisualResult> visualResultProperty();
    default void setVisualResult(VisualResult visualResult) { visualResultProperty().setValue(visualResult); }
    default VisualResult getVisualResult() { return visualResultProperty().getValue(); }

}
