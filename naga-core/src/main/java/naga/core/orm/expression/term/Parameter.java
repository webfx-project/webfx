package naga.core.orm.expression.term;

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

    private Object getParameterValue() {
        return getDataReader().getParameterValue(name);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Object evaluate(Object data) {
        Object value = getParameterValue();
        if (rightDot != null) {
            data = getDataReader().getData(value);
            value = rightDot.evaluate(data);
        }
        return value;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void setValue(Object data, Object value) {
        getDataWriter().setParameterValue(name, value);
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
