package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.lci.DataReader;
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
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return dataReader.getDomainObjectId(domainObject);
    }

}
