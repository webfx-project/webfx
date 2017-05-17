package naga.framework.expression.terms;

import naga.commons.type.Type;
import naga.framework.expression.lci.DataReader;

/**
 * @author Bruno Salmon
 */
public class This<T> extends AbstractExpression<T> {

    public static This SINGLETON = new This();

    private This() {
        super(9);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return domainObject;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append("this");
    }
}
