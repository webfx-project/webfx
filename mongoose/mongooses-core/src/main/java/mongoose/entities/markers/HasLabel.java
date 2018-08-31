package mongoose.entities.markers;

import mongoose.entities.Label;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasLabel {

    void setLabel(Object item);

    EntityId getLabelId();

    Label getLabel();

}
