package naga.framework.orm.entity;

import naga.util.collection.ListMixin;
import naga.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface EntityListMixin<E extends Entity> extends EntityList<E>, ListMixin<E> {

    EntityList<E> getEntityList();

    default EntityList<E> getList() {
        return getEntityList();
    }

    @Override
    default Object getListId() {
        return getEntityList().getListId();
    }

    @Override
    default EntityStore getStore() {
        return getEntityList().getStore();
    }

    @Override
    default void orderBy(Expression<E>... orderExpressions) {
        getEntityList().orderBy(orderExpressions);
    }
}
