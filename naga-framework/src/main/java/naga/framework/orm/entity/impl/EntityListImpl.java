package naga.framework.orm.entity.impl;

import naga.commons.util.collection.Collections;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.Ordered;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public class EntityListImpl<E extends Entity> implements EntityList<E> {

    private final Object listId;
    private final EntityStore store;
    private ArrayList<E> list = new ArrayList<>();

    public EntityListImpl(Object listId, EntityStore store) {
        this.listId = listId;
        this.store = store;
    }

    @Override
    public Object getListId() {
        return listId;
    }

    @Override
    public EntityStore getStore() {
        return store;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void add(E entity) {
        list.add(entity);
    }

    @Override
    public void orderBy(Expression<E>... orderExpressions) {
        if (orderExpressions.length == 1 && orderExpressions[0] instanceof ExpressionArray)
            orderBy(((ExpressionArray) orderExpressions[0]).getExpressions());
        else java.util.Collections.sort(list, (e1, e2) -> {
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
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public String toString() {
        return Collections.toStringWithLineFeeds(this);
    }
}
