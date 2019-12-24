package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;

/**
 * @author Bruno Salmon
 */
public abstract class BinaryExpression<T> extends AbstractExpression<T> {

    protected final Expression<T> left;
    protected final Expression<T> right;
    protected final String separator;

    public BinaryExpression(Expression<T> left, String separator, Expression<T> right, int precedenceLevel) {
        super(precedenceLevel);
        if (left == null || right == null)
            throw new IllegalArgumentException("An operand can't be null");
        this.left = left;
        this.right = right;
        this.separator = separator;
    }

    public Expression<T> getLeft() {
        return left;
    }

    public Expression<T> getRight() {
        return right;
    }

    public String getSeparator() {
        return separator;
    }

    @Override
    public Expression<?> getForwardingTypeExpression() {
        return left;
    }

    @Override
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        Object leftValue = left.evaluate(domainObject, domainReader);
        if (isShortcutValue(leftValue))
            return leftValue;
        Object rightValue = right.evaluate(domainObject, domainReader);
        if (isShortcutValue(rightValue))
            return rightValue;
        return evaluate(leftValue, rightValue, domainReader);
    }

    public boolean isShortcutValue(Object value) {
        return false;
    }


    public abstract Object evaluate(Object leftValue, Object rightValue, DomainReader<T> domainReader);

    @Override
    public StringBuilder toString(StringBuilder sb) {
        boolean lowerLeftPrecedence = left.getPrecedenceLevel() < getPrecedenceLevel();
        if (lowerLeftPrecedence)
            sb.append(/*left instanceof  As ? '[' : */ '('); // parser As issue workaround
        left.toString(sb);
        if (lowerLeftPrecedence)
            sb.append(/*left instanceof  As ? ']' : */ ')'); // parser As issue workaround
        sb.append(separator);
        boolean lowerRightPrecedence = right.getPrecedenceLevel() < getPrecedenceLevel();
        if (lowerRightPrecedence)
            sb.append('(');
        right.toString(sb);
        if (lowerRightPrecedence)
            sb.append(')');
        return sb;
    }

    @Override
    public void collect(CollectOptions options) {
        left.collect(options);
        right.collect(options);
    }
}
