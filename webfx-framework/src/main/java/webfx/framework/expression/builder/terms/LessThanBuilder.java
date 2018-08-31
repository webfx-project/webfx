package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.LessThan;

/**
 * @author Bruno Salmon
 */
public class LessThanBuilder extends BinaryBooleanExpressionBuilder {

    public LessThanBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected LessThan newBinaryOperation(Expression left, Expression right) {
        return new LessThan(left, right);
    }
}
