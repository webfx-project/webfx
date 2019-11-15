package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.LessThan;

/**
 * @author Bruno Salmon
 */
public final class LessThanBuilder extends BinaryBooleanExpressionBuilder {

    public LessThanBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected LessThan newBinaryOperation(Expression left, Expression right) {
        return new LessThan(left, right);
    }
}
