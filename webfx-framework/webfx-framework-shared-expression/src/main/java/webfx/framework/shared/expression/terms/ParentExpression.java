package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ParentExpression<T> extends Expression<T> {

    Expression<T>[] getChildren();

}
