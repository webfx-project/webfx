package naga.framework.orm.entity.impl;

import naga.commons.util.collection.Collections;
import naga.framework.expression.Expression;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;

import java.util.ArrayList;

/**
 * @author Bruno Salmon
 */
public class EntityListImpl<E extends Entity> extends ArrayList<E> implements EntityList<E> {

    private final Object listId;
    private final EntityStore store;

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
    public void orderBy(Expression<E>... orderExpressions) {
        EntityList.orderBy(this, orderExpressions);
    }

    @Override
    public String toString() {
        return Collections.toStringWithLineFeeds(this);
    }
}
