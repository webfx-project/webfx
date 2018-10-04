package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.lci.DataReader;
import webfx.fxkit.extra.type.Type;
import webfx.framework.shared.expression.terms.function.DomainClassType;

/**
 * @author Bruno Salmon
 */
public class Alias<T> extends AbstractExpression<T> {

    private final String name;
    private final Type type;
    private final Object domainClass;

    public Alias(String name, Type type) {
        this(name, type, type instanceof DomainClassType ? ((DomainClassType) type).getDomainClass() : null);
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
        // Reading the alias field which is supposed to be stored in the domain object
        return dataReader.getDomainFieldValue(domainObject, name);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append(name);
    }

}
