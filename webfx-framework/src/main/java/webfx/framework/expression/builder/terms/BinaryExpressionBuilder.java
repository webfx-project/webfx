package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.BinaryExpression;

/**
 * @author Bruno Salmon
 */
public abstract class BinaryExpressionBuilder extends ExpressionBuilder {
    public ExpressionBuilder left;
    public ExpressionBuilder right;

    protected BinaryExpression operation;

    public BinaryExpressionBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public BinaryExpression build() {
        if (operation == null) {
            propagateDomainClasses();
            operation = newBinaryOperation(left.build(), right.build());
        }
        return operation;
    }

    @Override
    protected void propagateDomainClasses() {
        super.propagateDomainClasses();
        left.buildingClass = buildingClass;
        right.buildingClass = buildingClass;
    }

    protected abstract BinaryExpression newBinaryOperation(Expression left, Expression right);
}
