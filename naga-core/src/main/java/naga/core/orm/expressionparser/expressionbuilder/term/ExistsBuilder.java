package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.term.Exists;

/**
 * @author Bruno Salmon
 */
public class ExistsBuilder extends ExpressionBuilder {
    public SelectBuilder select;

    private Exists exists;

    public ExistsBuilder(SelectBuilder select) {
        this.select = select;
    }

    @Override
    public Exists build() {
        if (exists == null)
            exists = new Exists(select.build());
        return exists;
    }
}
