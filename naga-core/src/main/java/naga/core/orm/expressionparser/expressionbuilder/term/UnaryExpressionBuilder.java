package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.UnaryExpression;

/**
 * @author Bruno Salmon
 */
public abstract class UnaryExpressionBuilder extends ExpressionBuilder {
    public ExpressionBuilder operand;

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
        operand.buildingClass = buildingClass;
    }

    protected abstract UnaryExpression newUnaryOperation(Expression operand);
}
