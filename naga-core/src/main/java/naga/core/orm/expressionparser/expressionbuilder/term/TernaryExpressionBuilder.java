package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.term.TernaryExpression;

/**
 * @author Bruno Salmon
 */
public class TernaryExpressionBuilder extends ExpressionBuilder {
    public ExpressionBuilder question;
    public ExpressionBuilder yes;
    public ExpressionBuilder no;

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
