package mongoose.entities;

import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Event extends Entity {

    void setName(String name);

    String getName();

    void setOrganization(Object organization);
}
