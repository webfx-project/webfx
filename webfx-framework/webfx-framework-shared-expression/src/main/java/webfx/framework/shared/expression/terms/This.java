package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.lci.DataReader;
import webfx.extras.type.Type;

/**
 * @author Bruno Salmon
 */
public final class This<T> extends AbstractExpression<T> {

    public static final This SINGLETON = new This();

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
