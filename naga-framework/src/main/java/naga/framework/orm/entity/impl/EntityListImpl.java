package naga.framework.orm.entity.impl;

import naga.commons.util.collection.Collections;
import naga.framework.expression.Expression;
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
        EntityList.orderBy(list, orderExpressions);
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
