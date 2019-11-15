package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.And;

/**
 * @author Bruno Salmon
 */
public final class AndBuilder extends BinaryExpressionBuilder {

    public AndBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected And newBinaryOperation(Expression left, Expression right) {
        return new And(left, right);
    }
}
