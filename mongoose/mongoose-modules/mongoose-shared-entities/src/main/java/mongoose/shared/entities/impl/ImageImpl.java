package mongoose.shared.entities.impl;

import mongoose.shared.entities.Image;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class ImageImpl extends DynamicEntity implements Image {

    public ImageImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
