package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.This;

/**
 * @author Bruno Salmon
 */
public class ThisBuilder extends ExpressionBuilder {

    public static ThisBuilder SINGLETON = new ThisBuilder();

    private ThisBuilder() {
    }

    @Override
    public Expression build() {
        return This.SINGLETON;
    }
}
