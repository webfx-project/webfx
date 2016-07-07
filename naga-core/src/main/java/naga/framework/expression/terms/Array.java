package naga.framework.expression.terms;

import naga.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class Array<T> extends UnaryExpression<T> {

    public Array(Expression<T> operand) {
        super(operand);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return operand.toString(sb.append('[')).append(']');
    }
}
