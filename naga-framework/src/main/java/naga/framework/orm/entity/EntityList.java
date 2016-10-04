package naga.framework.orm.entity;

import naga.framework.orm.entity.impl.EntityListImpl;

/**
 * @author Bruno Salmon
 */
public interface EntityList<E extends Entity> extends Iterable<E> {

    Object getListId();

    EntityStore getStore();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    E get(int index);

    void clear();

    void add(E entity);

    static <E extends Entity> EntityList<E> create(Object listId, EntityStore store) {
        return new EntityListImpl<>(listId, store);
    }
}
