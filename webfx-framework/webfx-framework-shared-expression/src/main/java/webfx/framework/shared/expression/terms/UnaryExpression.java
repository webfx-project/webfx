package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.lci.DataReader;

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
    public Expression<T> getForwardingTypeExpression() {
        return operand;
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
