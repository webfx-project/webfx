package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.UnaryExpression;
import naga.framework.expression.terms.function.Call;

/**
 * @author Bruno Salmon
 */
public class ImageExpressionBuilder extends UnaryExpressionBuilder {

    public ImageExpressionBuilder(ExpressionBuilder operand) {
        super(operand);
    }

    @Override
    protected UnaryExpression newUnaryOperation(Expression operand) {
        return new Call("image", operand); // temporary
    }
}
