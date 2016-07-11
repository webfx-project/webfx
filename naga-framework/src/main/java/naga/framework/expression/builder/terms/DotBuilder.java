package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Dot;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.Symbol;
import naga.framework.expression.builder.ReferenceResolver;
import naga.framework.expression.builder.ThreadLocalReferenceResolver;

/**
 * @author Bruno Salmon
 */
public class DotBuilder extends BinaryExpressionBuilder {
    private final boolean outerJoin;

    public DotBuilder(ExpressionBuilder left, ExpressionBuilder right, boolean outerJoin) {
        super(left, right);
        this.outerJoin = outerJoin;
    }

    @Override
    public Dot build() {
        if (operation == null) {
            propagateDomainClasses();
            Symbol leftExpression = (Symbol) left.build(); // left should be a term when using dot
            right.buildingClass = getModelReader().getSymbolForeignDomainClass(left.buildingClass, leftExpression);
            boolean leftResolver = left instanceof ReferenceResolver;
            if (leftResolver)
                ThreadLocalReferenceResolver.pushReferenceResolver((ReferenceResolver) left);
            Expression rightExpression = right.build();
            if (leftResolver)
                ThreadLocalReferenceResolver.popReferenceResolver();
            while (rightExpression instanceof ExpressionArray) {
                Expression[] expressions = ((ExpressionArray) rightExpression).getExpressions();
                if (expressions.length != 1)
                    break;
                rightExpression = expressions[0];
            }
            operation = newBinaryOperation(leftExpression, rightExpression);
        }
        return (Dot) operation;
    }

    @Override
    protected void propagateDomainClasses() {
        super.propagateDomainClasses();
    }

    @Override
    protected Dot newBinaryOperation(Expression left, Expression right) {
        return new Dot(left, right, outerJoin);
    }

}
