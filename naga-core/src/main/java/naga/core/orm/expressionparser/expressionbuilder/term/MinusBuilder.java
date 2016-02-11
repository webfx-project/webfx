package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Minus;

/**
 * @author Bruno Salmon
 */
public class MinusBuilder extends BinaryExpressionBuilder {

    public MinusBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Minus newBinaryOperation(Expression left, Expression right) {
        return new Minus(left, right);
    }
}
