package mongooses.core.entities.markers;

import mongooses.core.entities.Label;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasLabel {

    void setLabel(Object item);

    EntityId getLabelId();

    Label getLabel();

}
