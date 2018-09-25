package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.lci.DataReader;
import webfx.framework.expression.lci.DataWriter;
import webfx.fxkits.extra.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class Parameter<T> extends AbstractExpression<T> {

    public final static Parameter UNNAMED_PARAMETER = new Parameter<>(null, null);

    private final String name;
    private final Expression<T> rightDot;

    private final int index = -1; // index of this parameter in a oqlOrder

    public Parameter(String name, Expression<T> rightDot) {
        super(9);
        this.name = name;
        this.rightDot = rightDot;
    }

    public String getName() {
        return name;
    }

    public Expression<T> getRightDot() {
        return rightDot;
    }

    public int getIndex() {
        return index;
    }

    private Object getParameterValue(DataReader<T> dataReader) {
        return dataReader.getParameterValue(name);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        Object value = getParameterValue(dataReader);
        if (rightDot != null) {
            domainObject = dataReader.getDomainObjectFromId(value, domainObject);
            value = rightDot.evaluate(domainObject, dataReader);
        }
        return value;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        dataWriter.setParameterValue(name, value);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append('?').append(name);
        if (rightDot != null) {
            sb.append('.');
            boolean lowerRightPrecedence = rightDot.getPrecedenceLevel() < 8; // DOT precedence
            if (lowerRightPrecedence)
                sb.append('(');
            rightDot.toString(sb);
            if (lowerRightPrecedence)
                sb.append(')');
        }
        return sb;
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        persistentTerms.add(this);
    }

}
