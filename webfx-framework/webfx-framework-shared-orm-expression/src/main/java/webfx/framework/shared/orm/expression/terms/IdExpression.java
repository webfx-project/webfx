package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.extras.type.PrimType;

/**
 * @author Bruno Salmon
 */
public final class IdExpression<T> extends Symbol<T> {

    public final static IdExpression singleton = new IdExpression();

    private IdExpression() {
        super("id", PrimType.LONG);
    }

    @Override
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        return domainReader.getDomainObjectId(domainObject);
    }

}
