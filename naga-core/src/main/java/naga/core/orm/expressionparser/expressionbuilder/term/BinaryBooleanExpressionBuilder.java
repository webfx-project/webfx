package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.BinaryBooleanExpression;

/**
 * @author Bruno Salmon
 */
public abstract class BinaryBooleanExpressionBuilder extends BinaryExpressionBuilder {

    public BinaryBooleanExpressionBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    public BinaryBooleanExpression build() {
        return (BinaryBooleanExpression) super.build();
    }

    protected abstract BinaryBooleanExpression newBinaryOperation(Expression left, Expression right);
}
