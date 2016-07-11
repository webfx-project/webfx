package naga.framework.expression.terms;

import naga.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ParentExpression<T> extends Expression<T> {

    Expression<T>[] getChildren();

}
