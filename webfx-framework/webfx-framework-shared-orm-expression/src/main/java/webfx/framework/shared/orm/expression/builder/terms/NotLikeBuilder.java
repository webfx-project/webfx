package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.NotLike;

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
