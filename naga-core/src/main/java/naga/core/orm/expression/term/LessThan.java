package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class LessThan extends BooleanExpression {

    public LessThan(Expression left, Expression right) {
        super(left, "<", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        return PrimType.isLessThan((Number) a, (Number) b);
    }

}
