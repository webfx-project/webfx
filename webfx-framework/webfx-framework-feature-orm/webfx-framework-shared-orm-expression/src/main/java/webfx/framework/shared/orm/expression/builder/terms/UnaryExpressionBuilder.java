package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.UnaryExpression;

/**
 * @author Bruno Salmon
 */
public abstract class UnaryExpressionBuilder extends ExpressionBuilder {
    public final ExpressionBuilder operand; // may be null, like for example a call with no argument such as: now()

    private UnaryExpression operation;

    protected UnaryExpressionBuilder(ExpressionBuilder operand) {
        this.operand = operand;
    }

    @Override
    public UnaryExpression build() {
        if (operation == null) {
            propagateDomainClasses();
            operation = newUnaryOperation(operand == null ? null : operand.build());
        }
        return operation;
    }

    @Override
    protected void propagateDomainClasses() {
        super.propagateDomainClasses();
        if (operand != null)
            operand.buildingClass = buildingClass;
    }

    protected abstract UnaryExpression newUnaryOperation(Expression operand);
}
