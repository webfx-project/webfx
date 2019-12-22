package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.terms.TernaryExpression;

/**
 * @author Bruno Salmon
 */
public final class TernaryExpressionBuilder extends ExpressionBuilder {
    public final ExpressionBuilder question;
    public final ExpressionBuilder yes;
    public final ExpressionBuilder no;

    private TernaryExpression operation;

    public TernaryExpressionBuilder(ExpressionBuilder question, ExpressionBuilder yes, ExpressionBuilder no) {
        this.question = question;
        this.yes = yes;
        this.no = no;
    }

    @Override
    public TernaryExpression build() {
        if (operation == null) {
            propagateDomainClasses();
            operation = new TernaryExpression(question.build(), yes.build(), no.build());
        }
        return operation;
    }

    @Override
    protected void propagateDomainClasses() {
        super.propagateDomainClasses();
        question.buildingClass = yes.buildingClass = no.buildingClass = buildingClass;
    }
}
