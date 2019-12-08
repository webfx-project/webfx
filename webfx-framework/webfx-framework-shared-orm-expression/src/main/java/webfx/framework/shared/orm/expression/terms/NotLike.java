package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;

/**
 * @author Bruno Salmon
 */
public final class NotLike<T> extends BinaryBooleanExpression<T> {

    public NotLike(Expression<T> left, Expression<T> right) {
        super(left, " not like ", right, 5);
    }

    @Override
    public Boolean evaluateCondition(Object a, Object b, DomainReader<T> domainReader) {
        return b instanceof String && !new Like.LikeImpl((String) b).compare(a);
    }

}
