package naga.framework.expression.terms;

import naga.framework.expression.Expression;

import java.util.Collection;

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
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
