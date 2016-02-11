package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.NotLike;

/**
 * @author Bruno Salmon
 */
public class NotLikeBuilder extends BooleanExpressionBuilder {

    public NotLikeBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected NotLike newBinaryOperation(Expression left, Expression right) {
        return new NotLike(left, right);
    }
}
