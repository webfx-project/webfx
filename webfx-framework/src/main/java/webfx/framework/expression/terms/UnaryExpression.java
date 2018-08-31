package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.lci.DataReader;
import webfx.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public abstract class UnaryExpression<T> extends AbstractExpression<T> {
    protected final Expression<T> operand;

    public UnaryExpression(Expression<T> operand) {
        super(4);
        this.operand = operand;
    }

    public Expression<T> getOperand() {
        return operand;
    }

    @Override
    public Type getType() {
        return operand.getType();
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return operand.evaluate(domainObject, dataReader);
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        operand.collectPersistentTerms(persistentTerms);
    }

}
