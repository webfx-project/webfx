package naga.core.orm.entity;

import naga.core.orm.entity.impl.EntityListImpl;

/**
 * @author Bruno Salmon
 */
public interface EntityList extends Iterable<Entity> {

    Object getListId();

    EntityStore getStore();

    int size();

    Entity get(int index);

    void clear();

    void add(Entity entity);

    static EntityList create(Object listId, EntityStore store) {
        return new EntityListImpl(listId, store);
    }
}
