package naga.framework.orm.entity;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Select;
import naga.framework.expression.terms.function.Call;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.lciimpl.EntityDataReader;
import naga.util.Booleans;
import naga.util.collection.Collections;
import naga.util.function.Predicate;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class Entities {

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
        return Call.orderBy(entityList, new EntityDataReader<>(entityList.get(0).getStore()), orderExpressions);
    }
}
