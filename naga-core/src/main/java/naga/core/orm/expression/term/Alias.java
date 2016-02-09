package naga.core.orm.expression.term;

import naga.core.orm.expression.datalci.DataReader;
import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public class Alias extends AbstractExpression {

    private final String name;
    private final Type type;
    private final Object domainClass;

    public Alias(String name, Type type) {
        this(name, type, null);
    }

    public Alias(String name, Object domainClass) {
        this(name, null, domainClass);
    }

    public Alias(String name, Type type, Object domainClass) {
        super(9);
        this.name = name;
        this.type = type;
        this.domainClass = domainClass;
    }

    public String getName() {
        return name;
    }

    public Object getDomainClass() {
        return domainClass;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append(name);
    }
}
