package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Plus;

/**
 * @author Bruno Salmon
 */
public class PlusBuilder extends BinaryExpressionBuilder {

    public PlusBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Plus newBinaryOperation(Expression left, Expression right) {
        return new Plus(left, right);
    }
}
