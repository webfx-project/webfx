package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.commons.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class SelectExpression<T> extends AbstractExpression<T> {
    protected final Select<T> select;

    public SelectExpression(Select<T> select) {
        super(9);
        this.select = select;
    }

    public Select<T> getSelect() {
        return select;
    }

    @Override
    public Type getType() {
        ExpressionArray fields = select.getFields();
        Expression[] expressions = fields.getExpressions();
        return expressions.length == 1 ? expressions[0].getType() : fields.getType();
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append('(');
        select.toString(sb);
        return sb.append(')');
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        persistentTerms.add(this);
    }

}

