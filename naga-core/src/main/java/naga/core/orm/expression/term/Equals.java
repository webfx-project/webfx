package naga.core.orm.expression.term;

import naga.core.type.PrimType;
import naga.core.util.Booleans;

/**
 * @author Bruno Salmon
 */
public class Equals extends BooleanExpression {

    public Equals(Expression left, Expression right) {
        super(left, "=", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        return PrimType.areEquivalent(a, b);
    }

    @Override
    public void setValue(Object data, Object value) {
        if (Booleans.isTrue(value))
            left.setValue(data, right.evaluate(data));
    }

}
