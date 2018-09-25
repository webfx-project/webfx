package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.Person;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasPerson {

    void setPerson(Object person);

    EntityId getPersonId();

    Person getPerson();

}
