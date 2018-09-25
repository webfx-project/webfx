package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Like;

/**
 * @author Bruno Salmon
 */
public final class LikeBuilder extends BinaryBooleanExpressionBuilder {

    public LikeBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Like newBinaryOperation(Expression left, Expression right) {
        return new Like(left, right);
    }
}
