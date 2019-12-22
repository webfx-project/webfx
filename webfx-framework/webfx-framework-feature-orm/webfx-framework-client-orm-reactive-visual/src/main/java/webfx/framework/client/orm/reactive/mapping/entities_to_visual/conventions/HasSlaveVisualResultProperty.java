package webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions;

import javafx.beans.property.ObjectProperty;
import webfx.extras.visual.VisualResult;

public interface HasSlaveVisualResultProperty {

    ObjectProperty<VisualResult> slaveVisualResultProperty();

    default VisualResult getSlaveVisualResult() { return slaveVisualResultProperty().getValue(); }

    default void setSlaveVisualResult(VisualResult value) { slaveVisualResultProperty().setValue(value); }

}
