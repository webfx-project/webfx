package mongoose.entities.markers;

import mongoose.entities.Image;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasImage {

    void setImage(Object event);

    EntityId getImageId();

    Image getImage();

}
