package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.LessThanOrEquals;

/**
 * @author Bruno Salmon
 */
public final class LessThanOrEqualsBuilder extends BinaryBooleanExpressionBuilder {

    public LessThanOrEqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected LessThanOrEquals newBinaryOperation(Expression left, Expression right) {
        return new LessThanOrEquals(left, right);
    }
}
