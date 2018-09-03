package mongooses.core.entities.markers;

import mongooses.core.entities.Person;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasPerson {

    void setPerson(Object person);

    EntityId getPersonId();

    Person getPerson();

}
