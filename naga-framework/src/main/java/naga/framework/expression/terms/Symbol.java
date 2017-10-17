package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.lci.DataWriter;
import naga.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class Symbol<T> extends AbstractExpression<T> {

    protected final String name;
    protected final Type type;
    protected Expression<T> expression;

    public Symbol(String name) {
        this(name, (Type) null);
    }

    public Symbol(String name, Type type) {
        this(name, type, null);
    }

    public Symbol(String name, Expression<T> expression) {
        this(name, expression.getType(), expression);
    }

    public Symbol(String name, Type type, Expression<T> expression) {
        super(9);
        this.name = name;
        this.type = type;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public Expression<T> getExpression() {
        return expression;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        if (getExpression() != null)
            return getExpression().evaluate(domainObject, dataReader);
        return dataReader.getDomainFieldValue(domainObject, name);
    }

    @Override
    public boolean isEditable() {
        if (getExpression() != null)
            return getExpression().isEditable();
        return true;
    }

    @Override
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        if (getExpression() != null)
            getExpression().setValue(domainObject, value, dataWriter);
        else
            dataWriter.setDomainFieldValue(domainObject, name, value);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        if (getExpression() != null)
            getExpression().collectPersistentTerms(persistentTerms);
        else
            persistentTerms.add(this);
    }
}
