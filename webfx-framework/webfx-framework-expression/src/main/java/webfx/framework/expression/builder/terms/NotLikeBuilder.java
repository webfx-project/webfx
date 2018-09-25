package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.NotLike;

/**
 * @author Bruno Salmon
 */
public final class NotLikeBuilder extends BinaryBooleanExpressionBuilder {

    public NotLikeBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected NotLike newBinaryOperation(Expression left, Expression right) {
        return new NotLike(left, right);
    }
}
