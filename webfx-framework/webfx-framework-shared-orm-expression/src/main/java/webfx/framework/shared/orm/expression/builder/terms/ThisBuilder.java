package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.This;

/**
 * @author Bruno Salmon
 */
public final class ThisBuilder extends ExpressionBuilder {

    public static final ThisBuilder SINGLETON = new ThisBuilder();

    private ThisBuilder() {
    }

    @Override
    public Expression build() {
        return This.SINGLETON;
    }
}
