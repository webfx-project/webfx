package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.term.SelectExpression;

/**
 * @author Bruno Salmon
 */
public class SelectExpressionBuilder extends ExpressionBuilder {
    public SelectBuilder select;

    private SelectExpression selectExpression;

    public SelectExpressionBuilder(SelectBuilder select) {
        this.select = select;
    }

    @Override
    public SelectExpression build() {
        if (selectExpression == null) {
            select.includeIdColumn = false;
            selectExpression = new SelectExpression(select.build());
        }
        return selectExpression;
    }
}
