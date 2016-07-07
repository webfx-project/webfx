package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Equals;

/**
 * @author Bruno Salmon
 */
public class EqualsBuilder extends BinaryBooleanExpressionBuilder {

    public EqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Equals newBinaryOperation(Expression left, Expression right) {
        return new Equals(left, right);
    }
}
