package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.This;

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
