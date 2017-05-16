package naga.framework.orm.entity;

import naga.framework.expression.Expression;
import naga.framework.orm.entity.impl.EntityListImpl;

import java.util.Collection;

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

    void orderBy(Expression<E>... orderExpressions);

    static <E extends Entity> EntityList<E> create(Object listId, EntityStore store) {
        return new EntityListImpl<>(listId, store);
    }

    static <E extends Entity> EntityList<E> create(Object listId, EntityStore store, Collection<E> collections) {
        EntityList<E> entities = create(listId, store);
        for (E e : collections)
            entities.add(e);
        return entities;
    }
}
