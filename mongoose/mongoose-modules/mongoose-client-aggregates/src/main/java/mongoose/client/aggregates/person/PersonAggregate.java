package mongoose.client.aggregates.person;

import mongoose.shared.entities.Person;
import webfx.framework.shared.orm.entity.EntityStore;

/**
 * @author Bruno Salmon
 */
public interface PersonAggregate {

    static PersonAggregate get(EntityStore store) {
        return PersonAggregateImpl.get(store);
    }

    static PersonAggregate getOrCreate(EntityStore store) {
        return PersonAggregateImpl.getOrCreate(store);
    }

    Person getPreselectionProfilePerson();

}
