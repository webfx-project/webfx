package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.And;

/**
 * @author Bruno Salmon
 */
public class AndBuilder extends BinaryExpressionBuilder {

    public AndBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected And newBinaryOperation(Expression left, Expression right) {
        return new And(left, right);
    }
}
