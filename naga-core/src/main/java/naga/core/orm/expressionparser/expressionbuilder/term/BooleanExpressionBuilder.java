package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.BooleanExpression;

/**
 * @author Bruno Salmon
 */
public abstract class BooleanExpressionBuilder extends BinaryExpressionBuilder {

    public BooleanExpressionBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    public BooleanExpression build() {
        return (BooleanExpression) super.build();
    }

    protected abstract BooleanExpression newBinaryOperation(Expression left, Expression right);
}
