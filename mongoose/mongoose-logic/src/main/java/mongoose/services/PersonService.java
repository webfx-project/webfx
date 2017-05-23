package mongoose.services;

import mongoose.entities.Person;
import naga.framework.orm.entity.EntityStore;

/**
 * @author Bruno Salmon
 */
public interface PersonService {

    static PersonService get(EntityStore store) {
        return PersonServiceImpl.get(store);
    }

    static PersonService getOrCreate(EntityStore store) {
        return PersonServiceImpl.getOrCreate(store);
    }

    Person getPreselectionProfilePerson();

}
