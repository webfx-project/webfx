package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class LessThan<T> extends BooleanExpression<T> {

    public LessThan(Expression<T> left, Expression<T> right) {
        super(left, "<", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        return PrimType.isLessThan((Number) a, (Number) b);
    }

}
