package naga.framework.orm.entity;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.Ordered;
import naga.framework.orm.entity.impl.EntityListImpl;

import java.util.Collection;
import java.util.List;

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

    static <E extends Entity> List<E> orderBy(List<E> entityList, Expression<E>... orderExpressions) {
        if (orderExpressions.length == 1 && orderExpressions[0] instanceof ExpressionArray)
            orderBy(entityList, ((ExpressionArray) orderExpressions[0]).getExpressions());
        else java.util.Collections.sort(entityList, (e1, e2) -> {
            if (e1 == e2)
                return 0;
            if (e1 == null)
                return 1;
            if (e2 == null)
                return -1;
            for (Expression<E> orderExpression : orderExpressions) {
                Object o1 = e1.evaluate(orderExpression);
                Object o2 = e2.evaluate(orderExpression);
                Ordered<E> ordered = orderExpression instanceof Ordered ? (Ordered<E>) orderExpression : null;
                int result;
                if (o1 == o2)
                    result = 0;
                else if (o1 == null || !(o1 instanceof Comparable))
                    result = ordered == null || ordered.isNullsLast() ? 1 : -1;
                else if (o2 == null || !(o2 instanceof Comparable))
                    result = ordered == null || ordered.isNullsLast() ? -1 : 1;
                else {
                    result = ((Comparable) o1).compareTo(o2);
                    if (ordered != null && ordered.isDescending())
                        result = -result;
                }
                if (result != 0)
                    return result;
            }
            return 0;
        });
        return entityList;
    }

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
