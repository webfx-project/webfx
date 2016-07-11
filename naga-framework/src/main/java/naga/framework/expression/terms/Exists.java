package naga.framework.expression.terms;

import naga.framework.expression.lci.DataReader;
import naga.commons.type.PrimType;
import naga.commons.type.Type;

/**
 * @author Bruno Salmon
 */
public class Exists extends SelectExpression {

    public Exists(Select select) {
        super(select);
    }

    @Override
    public Type getType() {
        return PrimType.BOOLEAN;
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        return null;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return super.toString(sb.append("exists"));
    }

}
