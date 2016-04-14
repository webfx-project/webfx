package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.LessThanOrEquals;

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
