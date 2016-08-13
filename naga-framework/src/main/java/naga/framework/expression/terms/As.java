package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;

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
        // Reading the alias field which is supposed to be stored in the domain object
        return dataReader.getDomainFieldValue(domainObject, alias);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return operand.toString(sb).append(" as ").append(alias);
    }

/* Temporary commented because this may result in collecting the same alias several times which is confusing for sql (ex: here and in StringFilter fields - See EventsActivity for example)
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        persistentTerms.add(this); // We want the whole As expression to be sent to the server
    }
*/

}
