package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Divide;

/**
 * @author Bruno Salmon
 */
public final class DivideBuilder extends BinaryExpressionBuilder {

    public DivideBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Divide newBinaryOperation(Expression left, Expression right) {
        return new Divide(left, right);
    }
}
