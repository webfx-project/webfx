package mongoose.entities;

import mongoose.entities.markers.EntityHasEvent;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Document extends Entity, EntityHasEvent {

    void setRef(Integer ref);

    Integer getRef();

    void setPersonFirstName(String personFirstName);

    String getPersonFirstName();

    void setPersonLastName(String personLastName);

    String getPersonLastName();

}
