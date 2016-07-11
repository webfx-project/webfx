package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Any;

/**
 * @author Bruno Salmon
 */
public class AnyBuilder extends BinaryBooleanExpressionBuilder {
    String operator;

    public AnyBuilder(ExpressionBuilder left, String operator, ExpressionBuilder right) {
        super(left, right);
        this.operator = operator;
    }

    @Override
    protected Any newBinaryOperation(Expression left, Expression right) {
        return new Any(left, operator, right);
    }
}
