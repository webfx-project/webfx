package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Equals;

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
