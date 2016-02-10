package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataReader;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class As<T> extends UnaryExpression<T> {

    private final String alias;

    public As(Expression<T> operand, String alias) {
        super(operand);
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return dataReader.getDomainFieldValue(domainObject, this);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        operand.toString(sb);
        sb.append(" as ").append(alias);
        return sb;
    }

    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        persistentTerms.add(this); // when using as we want all to be sent to the server
    }

}
