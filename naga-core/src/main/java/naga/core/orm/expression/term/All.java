package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class All extends BooleanExpression {

    public All(Expression left, String operator, Expression right) {
        super(left, operator + " all ", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        throw new UnsupportedOperationException();
    }

}
