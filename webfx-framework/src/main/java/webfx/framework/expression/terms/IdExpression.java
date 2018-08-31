package webfx.framework.expression.terms;

import webfx.framework.expression.lci.DataReader;
import webfx.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class IdExpression<T> extends Symbol<T> {

    public final static IdExpression singleton = new IdExpression();

    private IdExpression() {
        super("id", PrimType.LONG);
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return dataReader.getDomainObjectId(domainObject);
    }

}
