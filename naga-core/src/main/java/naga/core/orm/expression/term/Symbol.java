package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.datalci.DataReader;
import naga.core.orm.expression.datalci.DataWriter;
import naga.core.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class Symbol extends AbstractExpression {

    protected final String name;
    protected final Type type;
    protected Expression expression;

    public Symbol(String name) {
        this(name, (Type) null);
    }

    public Symbol(String name, Type type) {
        this(name, type, null);
    }

    public Symbol(String name, Expression expression) {
        this(name, expression.getType(), expression);
    }

    public Symbol(String name, Type type, Expression expression) {
        super(9);
        this.name = name;
        this.type = type;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
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
    public void setValue(Object domainObject, Object value, DataWriter dataWriter) {
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
    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        if (getExpression() != null)
            getExpression().collectPersistentTerms(persistentTerms);
        else
            persistentTerms.add(this);
    }
}
