package naga.framework.orm.entity;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.Ordered;
import naga.framework.expression.terms.Select;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.DomainModel;
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
        if (orderExpressions.length == 1 && orderExpressions[0] instanceof ExpressionArray)
            orderBy(entityList, ((ExpressionArray) orderExpressions[0]).getExpressions());
        else
            entityList.sort((e1, e2) -> {
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
}
