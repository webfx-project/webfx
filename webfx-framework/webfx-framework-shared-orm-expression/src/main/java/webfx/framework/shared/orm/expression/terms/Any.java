package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;

/**
 * @author Bruno Salmon
 */
public final class Any<T> extends BinaryBooleanExpression<T> {

    public Any(Expression<T> left, String operator, Expression<T> right) {
        super(left, operator + " any ", right, 5);
    }

    public Boolean evaluateCondition(Object a, Object b, DomainReader<T> domainReader) {
        throw new UnsupportedOperationException();
    }
}
