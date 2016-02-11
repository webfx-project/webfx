package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.UnaryExpression;
import naga.core.orm.expression.term.function.Call;

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
