package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class In<T> extends BooleanExpression<T> {

    public In(Expression<T> left, Expression<T> right) {
        super(left, " in ", right, 5);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return b instanceof List && ((List) b).contains(a);
    }
}
