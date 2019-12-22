package webfx.framework.shared.orm.entity;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Select;
import webfx.framework.shared.orm.entity.impl.EntityListImpl;
import java.util.function.Predicate;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface EntityList<E extends Entity> extends List<E>, HasEntityStore {

    Object getListId();

    void orderBy(Expression<E>... orderExpressions);

    default List<E> filter(String filterExpression) {
        return Entities.filter(this, filterExpression);
    }

    default List<E> filter(String filterExpression, Object entityClassId) {
        return Entities.filter(this, filterExpression, entityClassId, getDomainModel());
    }

    default List<E> filter(Expression<E> filterExpression) {
        return Entities.filter(this, filterExpression);
    }

    default List<E> filter(Predicate<? super E> predicate) {
        return Entities.filter(this, predicate);
    }

    default List<E> select(String select) {
        return Entities.select(this, select, getDomainModel());
    }

    default List<E> select(Select<E> select) {
        return Entities.select(this, select);
    }


    // static factoy methods

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
