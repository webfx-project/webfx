package webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions;

import javafx.beans.property.ObjectProperty;
import webfx.extras.visual.VisualResult;

public interface HasGenericVisualResultProperty {

    ObjectProperty<VisualResult> genericVisualResultProperty();

    default VisualResult getGenericVisualResult() { return genericVisualResultProperty().getValue(); }

    default void setGenericVisualResult(VisualResult value) { genericVisualResultProperty().setValue(value); }

}
