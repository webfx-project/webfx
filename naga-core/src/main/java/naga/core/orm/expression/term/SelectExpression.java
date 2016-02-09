package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.datalci.DataReader;
import naga.core.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class SelectExpression extends AbstractExpression {
    protected final Select select;

    public SelectExpression(Select select) {
        super(9);
        this.select = select;
    }

    public Select getSelect() {
        return select;
    }

    @Override
    public Type getType() {
        ExpressionArray fields = select.getFields();
        Expression[] expressions = fields.getExpressions();
        return expressions.length == 1 ? expressions[0].getType() : fields.getType();
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append('(');
        select.toString(sb);
        return sb.append(')');
    }

    @Override
    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        persistentTerms.add(this);
    }

}

