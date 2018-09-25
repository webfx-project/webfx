package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.UnaryExpression;
import webfx.framework.expression.terms.function.Call;

/**
 * @author Bruno Salmon
 */
public final class ImageExpressionBuilder extends UnaryExpressionBuilder {

    public ImageExpressionBuilder(ExpressionBuilder operand) {
        super(operand);
    }

    @Override
    protected UnaryExpression newUnaryOperation(Expression operand) {
        return new Call("image", operand); // temporary
    }
}
