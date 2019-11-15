package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.terms.SelectExpression;

/**
 * @author Bruno Salmon
 */
public final class SelectExpressionBuilder extends ExpressionBuilder {
    public final SelectBuilder select;

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
