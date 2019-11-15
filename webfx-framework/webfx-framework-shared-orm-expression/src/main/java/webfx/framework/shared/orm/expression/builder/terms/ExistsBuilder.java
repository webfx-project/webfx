package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.terms.Exists;

/**
 * @author Bruno Salmon
 */
public final class ExistsBuilder extends ExpressionBuilder {

    public final SelectBuilder select;

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
