package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Or;

/**
 * @author Bruno Salmon
 */
public class OrBuilder extends BinaryExpressionBuilder {

    public OrBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Or newBinaryOperation(Expression left, Expression right) {
        return new Or(left, right);
    }
}
