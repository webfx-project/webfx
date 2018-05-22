package mongoose.aggregates;

import mongoose.entities.Person;
import naga.framework.orm.entity.EntityStore;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class PersonAggregateImpl implements PersonAggregate {

    private static Map<Object, PersonAggregate> aggregates = new IdentityHashMap<>();

    static PersonAggregate get(EntityStore store) {
        return aggregates.get(store);
    }

    static PersonAggregate getOrCreate(EntityStore store) {
        PersonAggregate service = get(store);
        if (service == null)
            aggregates.put(store, service = new PersonAggregateImpl(store));
        return service;
    }

    private final EntityStore store;
    private Person preselectionProfilePerson;

    public PersonAggregateImpl(EntityStore store) {
        this.store = store;
        preselectionProfilePerson = store.createEntity(Person.class);
    }

    @Override
    public Person getPreselectionProfilePerson() {
        return preselectionProfilePerson;
    }
}
