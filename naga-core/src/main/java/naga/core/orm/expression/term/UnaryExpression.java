package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataReader;
import naga.core.type.Type;

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
