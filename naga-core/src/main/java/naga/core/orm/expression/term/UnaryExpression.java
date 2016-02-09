package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.datalci.DataReader;
import naga.core.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public abstract class UnaryExpression extends AbstractExpression {
    protected final Expression operand;

    public UnaryExpression(Expression operand) {
        super(4);
        this.operand = operand;
    }

    public Expression getOperand() {
        return operand;
    }

    @Override
    public Type getType() {
        return operand.getType();
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        return operand.evaluate(domainObject, dataReader);
    }

    @Override
    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        operand.collectPersistentTerms(persistentTerms);
    }

}
