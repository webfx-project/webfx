package naga.core.orm.expression.term;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class In extends BooleanExpression {

    public In(Expression left, Expression right) {
        super(left, " in ", right, 5);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return b instanceof List && ((List) b).contains(a);
    }
}
