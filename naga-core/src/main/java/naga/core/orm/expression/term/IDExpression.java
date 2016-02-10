package naga.core.orm.expression.term;

import naga.core.orm.expression.lci.DataReader;
import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class IDExpression<T> extends Symbol<T> {

    public final static IDExpression singleton = new IDExpression();

    private IDExpression() {
        super("id", PrimType.LONG);
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return dataReader.getDomainObjectId(domainObject);
    }

}
