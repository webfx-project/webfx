package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.NotEquals;

/**
 * @author Bruno Salmon
 */
public class NotEqualsBuilder extends BinaryBooleanExpressionBuilder {

    public NotEqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected NotEquals newBinaryOperation(Expression left, Expression right) {
        return new NotEquals(left, right);
    }
}
