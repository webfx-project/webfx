package naga.framework.expression.builder.terms;

import naga.framework.expression.terms.SelectExpression;

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
