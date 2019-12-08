package webfx.framework.shared.orm.entity;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Select;
import webfx.framework.shared.orm.expression.terms.function.Call;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.lciimpl.EntityDomainReader;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.collection.Collections;
import java.util.function.Predicate;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class Entities {

    public static EntityId getId(Entity entity) {
        return entity == null ? null : entity.getId();
    }

    public static Object getPrimaryKey(Entity entity) {
        return entity == null ? null : entity.getPrimaryKey();
    }

    public static Object getPrimaryKey(EntityId entityId) {
        return entityId == null ? null : entityId.getPrimaryKey();
    }

    public static Object getPrimaryKey(Object o) {
        return o instanceof Entity ? ((Entity) o).getPrimaryKey() : o instanceof EntityId ? ((EntityId) o).getPrimaryKey() : o;
    }

    public static boolean isNew(Entity entity) {
        return entity != null && entity.isNew();
    }

    public static boolean isNotNew(Entity entity) {
        return entity != null && !entity.isNew();
    }

    public static boolean sameId(Entity e1, Entity e2) {
        return e1 == e2 || e1 != null && e2 != null && e1.getId().equals(e2.getId());
    }

    public static <E extends Entity> List<E> filter(List<E> entityList, String filterExpression) {
        if (Collections.isEmpty(entityList))
            return Collections.emptyList();
        DomainClass domainClass = entityList.get(0).getDomainClass();
        return filter(entityList, filterExpression, domainClass.getId(), domainClass.getDomainModel());
    }

    public static <E extends Entity> List<E> filter(List<E> entityList, String filterExpression, Object entityClassId, DomainModel domainModel) {
        return Collections.isEmpty(entityList) ? Collections.emptyList() : filter(entityList, domainModel.parseExpression(filterExpression, entityClassId));
    }

    public static <E extends Entity> List<E> filter(List<E> entityList, Expression<E> filterExpression) {
        return filter(entityList, e -> Booleans.isTrue(e.evaluate(filterExpression)));
    }

    public static <E extends Entity> List<E> filter(List<E> entityList, Predicate<? super E> predicate) {
        return Collections.filter(entityList, predicate);
    }

    public static <E extends Entity> List<E> select(List<E> entityList, String select) {
        if (Collections.isEmpty(entityList))
            return Collections.emptyList();
        return select(entityList, select, entityList.get(0).getDomainClass().getDomainModel());
    }

    public static <E extends Entity> List<E> select(List<E> entityList, String select, DomainModel domainModel) {
        if (Collections.isEmpty(entityList))
            return Collections.emptyList();
        return select(entityList, domainModel.parseSelect(select));
    }

    public static <E extends Entity> List<E> select(List<E> entityList, Select<E> select) {
        return filter(entityList, select.getWhere());
    }

    public static <E extends Entity> List<E> orderBy(List<E> entityList, Expression<E>... orderExpressions) {
        if (Collections.isEmpty(entityList))
            return Collections.emptyList();
        return Call.orderBy(entityList, new EntityDomainReader<>(entityList.get(0).getStore()), orderExpressions);
    }
}
