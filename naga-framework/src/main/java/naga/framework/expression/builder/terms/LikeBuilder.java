package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Like;

/**
 * @author Bruno Salmon
 */
public class LikeBuilder extends BinaryBooleanExpressionBuilder {

    public LikeBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Like newBinaryOperation(Expression left, Expression right) {
        return new Like(left, right);
    }
}
