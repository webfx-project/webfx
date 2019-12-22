package webfx.framework.shared.orm.entity;

import webfx.platform.shared.util.collection.ListMixin;
import webfx.framework.shared.orm.expression.Expression;

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
