package webfx.framework.shared.orm.expression.terms;

import webfx.extras.type.Type;
import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.terms.function.DomainClassType;

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
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        // Reading the alias field which is supposed to be stored in the domain object
        return domainReader.getDomainFieldValue(domainObject, name);
    }

    @Override
    public void collect(CollectOptions options) {
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append(name);
    }

}
