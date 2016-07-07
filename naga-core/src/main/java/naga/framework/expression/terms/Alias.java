package naga.framework.expression.terms;

import naga.framework.expression.lci.DataReader;
import naga.commons.type.Type;

/**
 * @author Bruno Salmon
 */
public class Alias<T> extends AbstractExpression<T> {

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
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append(name);
    }

}
