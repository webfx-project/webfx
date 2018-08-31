package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ParentExpression<T> extends Expression<T> {

    Expression<T>[] getChildren();

}
