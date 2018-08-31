package webfx.framework.expression.terms.function;

import webfx.type.Type;

/**
 * @author Bruno Salmon
 */
public class DomainClassType implements Type {

    private final Object domainClass;

    public DomainClassType(Object domainClass) {
        this.domainClass = domainClass;
    }

    public Object getDomainClass() {
        return domainClass;
    }
}
