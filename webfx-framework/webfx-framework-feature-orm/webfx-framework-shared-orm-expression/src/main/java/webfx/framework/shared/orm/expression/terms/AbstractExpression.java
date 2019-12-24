package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractExpression<T> implements Expression<T> {

    private final int precedenceLevel;

    public AbstractExpression(int precedenceLevel) {
        this.precedenceLevel = precedenceLevel;
    }


    @Override
    public int getPrecedenceLevel() {
        return precedenceLevel;
    }


    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
