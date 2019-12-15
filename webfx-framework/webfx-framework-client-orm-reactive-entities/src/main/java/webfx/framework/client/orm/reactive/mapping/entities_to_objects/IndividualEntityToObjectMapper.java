package webfx.framework.client.orm.reactive.mapping.entities_to_objects;

import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface IndividualEntityToObjectMapper<E extends Entity, T> {

    T getMappedObject();

    void onEntityChangedOrReplaced(E entity);

    void onEntityRemoved(E entity);

}
