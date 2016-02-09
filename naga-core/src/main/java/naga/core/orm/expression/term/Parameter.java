package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.datalci.DataReader;
import naga.core.orm.expression.datalci.DataWriter;
import naga.core.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class Parameter extends AbstractExpression {

    private final String name;
    private final Expression rightDot;

    private int index = -1; // index of this parameter in a oqlOrder

    public Parameter(String name, Expression rightDot) {
        super(9);
        this.name = name;
        this.rightDot = rightDot;
    }

    public String getName() {
        return name;
    }

    public Expression getRightDot() {
        return rightDot;
    }

    public int getIndex() {
        return index;
    }

    private Object getParameterValue(DataReader dataReader) {
        return dataReader.getParameterValue(name);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        Object value = getParameterValue(dataReader);
        if (rightDot != null) {
            domainObject = dataReader.getDomainObjectFromId(value);
            value = rightDot.evaluate(domainObject, dataReader);
        }
        return value;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void setValue(Object domainObject, Object value, DataWriter dataWriter) {
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
    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        persistentTerms.add(this);
    }

}
