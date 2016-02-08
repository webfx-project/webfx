package naga.core.orm.expression.term;

import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class GreaterThan extends BooleanExpression {

    public GreaterThan(Expression left, Expression right) {
        super(left, ">", right, 5);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return PrimType.isGreaterThan((Number) a, (Number) b);
    }
}
