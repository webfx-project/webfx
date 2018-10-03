package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.Expression;

/**
 * @author Bruno Salmon
 */
public final class Array<T> extends UnaryExpression<T> {

    public Array(Expression<T> operand) {
        super(operand);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return operand.toString(sb.append('[')).append(']');
    }
}
