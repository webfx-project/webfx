package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.NotEquals;

/**
 * @author Bruno Salmon
 */
public final class NotEqualsBuilder extends BinaryBooleanExpressionBuilder {

    public NotEqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected NotEquals newBinaryOperation(Expression left, Expression right) {
        return new NotEquals(left, right);
    }
}
