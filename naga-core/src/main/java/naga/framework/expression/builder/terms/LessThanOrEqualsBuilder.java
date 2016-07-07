package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.LessThanOrEquals;

/**
 * @author Bruno Salmon
 */
public class LessThanOrEqualsBuilder extends BinaryBooleanExpressionBuilder {

    public LessThanOrEqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected LessThanOrEquals newBinaryOperation(Expression left, Expression right) {
        return new LessThanOrEquals(left, right);
    }
}
