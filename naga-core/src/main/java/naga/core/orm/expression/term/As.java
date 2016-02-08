package naga.core.orm.expression.term;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class As extends UnaryExpression {
    private final String alias;

    public As(Expression operand, String alias) {
        super(operand);
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public Object evaluate(Object data) {
        return getDataReader().getValue(data, this);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        operand.toString(sb);
        sb.append(" as ").append(alias);
        return sb;
    }

    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        persistentTerms.add(this); // when using as we want all to be sent to the server
    }

}
