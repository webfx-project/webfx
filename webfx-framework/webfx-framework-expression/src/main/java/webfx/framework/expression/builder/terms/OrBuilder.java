package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Or;

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
