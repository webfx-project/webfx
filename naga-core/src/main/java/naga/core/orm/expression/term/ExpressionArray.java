package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataReader;
import naga.core.type.ArrayType;
import naga.core.type.Type;
import naga.core.type.Types;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class ExpressionArray<T> extends AbstractExpression<T> implements ParentExpression<T> {

    protected final Expression<T>[] expressions;

    public ExpressionArray(Collection<Expression> expressions) {
        this(expressions.toArray(new Expression[expressions.size()]));
    }

    public ExpressionArray(Expression<T>... expressions) {
        super(1);
        this.expressions = expressions;
    }

    public Expression<T>[] getExpressions() {
        return expressions;
    }

    @Override
    public Expression<T>[] getChildren() {
        return expressions;
    }

    @Override
    public ArrayType getType() {
        Type[] types = new Type[expressions.length];
        for (int i = 0; i < expressions.length; i++)
            types[i] = expressions[i].getType();
        return Types.arrayType(types);
    }

    @Override
    public Object[] evaluate(T domainObject, DataReader<T> dataReader) {
        Object[] values = new Object[expressions.length];
        for (int i = 0; i < expressions.length; i++)
            values[i] = expressions[i].evaluate(domainObject, dataReader);
        return values;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        for (int i = 0; i < expressions.length; i++) {
            if (i != 0)
                sb.append(",");
            expressions[i].toString(sb);
        }
        return sb;
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        for (Expression expression : expressions)
            expression.collectPersistentTerms(persistentTerms);
    }

}
