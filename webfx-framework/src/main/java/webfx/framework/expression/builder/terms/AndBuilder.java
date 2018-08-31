package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.And;

/**
 * @author Bruno Salmon
 */
public class AndBuilder extends BinaryExpressionBuilder {

    public AndBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected And newBinaryOperation(Expression left, Expression right) {
        return new And(left, right);
    }
}
