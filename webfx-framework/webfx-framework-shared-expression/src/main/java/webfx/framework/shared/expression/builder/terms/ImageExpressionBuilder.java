package webfx.framework.shared.expression.builder.terms;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.UnaryExpression;
import webfx.framework.shared.expression.terms.function.Call;

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
