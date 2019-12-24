package webfx.framework.shared.orm.expression.terms;

import webfx.extras.type.Type;
import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.lci.DomainWriter;

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

    private Object getParameterValue(DomainReader<T> domainReader) {
        return domainReader.getParameterValue(name);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        Object value = getParameterValue(domainReader);
        if (rightDot != null) {
            domainObject = domainReader.getDomainObjectFromId(value, domainObject);
            value = rightDot.evaluate(domainObject, domainReader);
        }
        return value;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void setValue(T domainObject, Object value, DomainWriter<T> dataWriter) {
        dataWriter.setParameterValue(name, value);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append('?');
        if (name != null)
            sb.append(name);
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
    public void collect(CollectOptions options) {
        if (options.includeParameter())
            options.addTerm(this);
    }
}
