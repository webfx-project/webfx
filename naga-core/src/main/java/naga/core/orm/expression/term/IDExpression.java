package naga.core.orm.expression.term;

import naga.core.orm.expression.datalci.DataReader;
import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class IDExpression extends Symbol {

    public final static IDExpression singleton = new IDExpression();

    private IDExpression() {
        super("id", PrimType.LONG);
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        return dataReader.getDomainObjectId(domainObject);
    }

}
