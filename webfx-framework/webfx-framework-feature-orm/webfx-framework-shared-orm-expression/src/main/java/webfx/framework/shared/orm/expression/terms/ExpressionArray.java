package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.lci.DomainWriter;
import webfx.extras.type.ArrayType;
import webfx.extras.type.Type;
import webfx.extras.type.Types;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class ExpressionArray<T> extends AbstractExpression<T> implements ParentExpression<T> {

    protected final Expression<T>[] expressions;

    public ExpressionArray(Collection<Expression<T>> expressions) {
        this(expressions.toArray(new Expression[expressions.size()]));
    }

    @SafeVarargs
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
    public Object[] evaluate(T domainObject, DomainReader<T> domainReader) {
        Object[] values = new Object[expressions.length];
        for (int i = 0; i < expressions.length; i++)
            values[i] = expressions[i].evaluate(domainObject, domainReader);
        return values;
    }

    @Override
    public void setValue(T domainObject, Object value, DomainWriter<T> dataWriter) {
        Object[] values = (Object[]) value;
        for (int i = 0; i < expressions.length; i++)
            expressions[i].setValue(domainObject, values[i], dataWriter);
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
    public void collect(CollectOptions options) {
        for (Expression<T> expression : expressions)
            expression.collect(options);
    }
}
