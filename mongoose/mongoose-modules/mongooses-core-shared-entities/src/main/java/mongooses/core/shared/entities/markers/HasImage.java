package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.Image;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasImage {

    void setImage(Object event);

    EntityId getImageId();

    Image getImage();

}
