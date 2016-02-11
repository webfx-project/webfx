package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Like;

/**
 * @author Bruno Salmon
 */
public class LikeBuilder extends BooleanExpressionBuilder {

    public LikeBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Like newBinaryOperation(Expression left, Expression right) {
        return new Like(left, right);
    }
}
