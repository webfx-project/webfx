package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.BinaryExpression;

/**
 * @author Bruno Salmon
 */
public abstract class BinaryExpressionBuilder extends ExpressionBuilder {
    public final ExpressionBuilder left;
    public final ExpressionBuilder right;

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
