package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.NotLike;

/**
 * @author Bruno Salmon
 */
public class NotLikeBuilder extends BinaryBooleanExpressionBuilder {

    public NotLikeBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected NotLike newBinaryOperation(Expression left, Expression right) {
        return new NotLike(left, right);
    }
}
