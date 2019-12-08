package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;

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
    public Expression<?> getForwardingTypeExpression() {
        return operand;
    }

    @Override
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        return operand.evaluate(domainObject, domainReader);
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        operand.collectPersistentTerms(persistentTerms);
    }

}
