package naga.core.orm.expression.term;

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
    public Object evaluate(Object data) {
        return operand.evaluate(data);
    }

    @Override
    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        operand.collectPersistentTerms(persistentTerms);
    }

}
