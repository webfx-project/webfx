package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.All;

/**
 * @author Bruno Salmon
 */
public final class AllBuilder extends BinaryBooleanExpressionBuilder {
    final String operator;

    public AllBuilder(ExpressionBuilder left, String operator, ExpressionBuilder right) {
        super(left, right);
        this.operator = operator;
    }

    @Override
    protected All newBinaryOperation(Expression left, Expression right) {
        return new All(left, operator, right);
    }
}
