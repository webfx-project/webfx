package mongoose.entities;

import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface Document extends Entity {

    void setEvent(Object event);

    EntityId getEventId();

    Event getEvent();

    void setRef(Integer ref);

    Integer getRef();

    void setPersonFirstName(String personFirstName);

    String getPersonFirstName();

    void setPersonLastName(String personLastName);

    String getPersonLastName();


}
