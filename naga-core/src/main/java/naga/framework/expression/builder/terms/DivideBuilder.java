package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Divide;

/**
 * @author Bruno Salmon
 */
public class DivideBuilder extends BinaryExpressionBuilder {

    public DivideBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Divide newBinaryOperation(Expression left, Expression right) {
        return new Divide(left, right);
    }
}
