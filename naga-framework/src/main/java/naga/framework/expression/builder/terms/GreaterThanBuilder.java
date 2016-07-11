package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.GreaterThan;

/**
 * @author Bruno Salmon
 */
public class GreaterThanBuilder extends BinaryBooleanExpressionBuilder {

    public GreaterThanBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected GreaterThan newBinaryOperation(Expression left, Expression right) {
        return new GreaterThan(left, right);
    }
}
