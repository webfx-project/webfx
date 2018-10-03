package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.lci.DataReader;
import webfx.fxkits.extra.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class SelectExpression<T> extends AbstractExpression<T> {
    protected final Select<T> select;
    private static int asSeq;
    private As as;

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
        if (as != null)
            return as.evaluate(domainObject, dataReader);
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
        if (as == null)
            as = new As(this, "as" + ++asSeq);
        persistentTerms.add(as);
    }

}

