package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.datalci.DataReader;
import naga.core.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public abstract class BinaryExpression extends AbstractExpression {
    protected final Expression left;
    protected final Expression right;
    protected final String separator;

    public BinaryExpression(Expression left, String separator, Expression right, int precedenceLevel) {
        super(precedenceLevel);
        this.left = left;
        this.right = right;
        this.separator = separator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public String getSeparator() {
        return separator;
    }

    @Override
    public Type getType() {
        return left.getType();
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        Object leftValue = left.evaluate(domainObject, dataReader);
        if (isShortcutValue(leftValue))
            return leftValue;
        Object rightValue = right.evaluate(domainObject, dataReader);
        if (isShortcutValue(rightValue))
            return rightValue;
        return evaluate(leftValue, rightValue);
    }

    public boolean isShortcutValue(Object value) {
        return false;
    }


    public abstract Object evaluate(Object leftValue, Object rightValue);

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

    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        left.collectPersistentTerms(persistentTerms);
        right.collectPersistentTerms(persistentTerms);
    }


}
