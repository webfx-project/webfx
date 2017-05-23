package mongoose.services;

import mongoose.entities.Person;
import naga.framework.orm.entity.EntityStore;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class PersonServiceImpl implements PersonService {

    private static Map<Object, PersonService> services = new IdentityHashMap<>();

    static PersonService get(EntityStore store) {
        return services.get(store);
    }

    static PersonService getOrCreate(EntityStore store) {
        PersonService service = get(store);
        if (service == null)
            services.put(store, service = new PersonServiceImpl(store));
        return service;
    }

    private final EntityStore store;
    private Person preselectionProfilePerson;

    public PersonServiceImpl(EntityStore store) {
        this.store = store;
        preselectionProfilePerson = store.createEntity(Person.class);
    }

    @Override
    public Person getPreselectionProfilePerson() {
        return preselectionProfilePerson;
    }
}
