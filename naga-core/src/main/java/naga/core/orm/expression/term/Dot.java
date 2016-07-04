package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataReader;
import naga.core.type.Type;
import naga.core.util.collection.HashList;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class Dot<T> extends BinaryExpression<T> {

    private final boolean outerJoin;
    private final boolean readLeftKey;

    public Dot(Expression<T> left, Expression<T> right) {
        this(left, right, false);
    }

    public Dot(Expression<T> left, Expression<T> right, boolean outerJoin) {
        this(left, right, outerJoin, true);
    }

    public Dot(Expression<T> left, Expression<T> right, boolean outerJoin, boolean readLeftKey) {
        //super(left instanceof Dot ? ((Dot) left).getLeft() : left, outerJoin ? ".." : ".", left instanceof Dot ? new Dot(((Dot) left).getRight(), right, outerJoin, readLeftKey) : right, 8);
        super(left, outerJoin ? ".." : ".", right, 8);
        this.outerJoin = outerJoin;
        this.readLeftKey = readLeftKey;
    }

    public boolean isOuterJoin() {
        return outerJoin;
    }

    public boolean isReadLeftKey() {
        return readLeftKey;
    }

    @Override
    public Type getType() {
        return right.getType();
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        Object leftValue = left.evaluate(domainObject, dataReader);
        if (leftValue == null)
            return null; // NoValue.singleton;
        /*if (leftValue instanceof SystemValue)
            return leftValue;*/
        T rightData = dataReader.getDomainObjectFromId(leftValue);
        return right == null ? null : right.evaluate(rightData, dataReader);
    }

    @Override
    public Object evaluate(Object leftValue, Object rightValue, DataReader<T> dataReader) {
        return null; // never called due to above evaluate method override
    }

    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        List<Expression<T>> rightPersistentTerms = new HashList<>();
        right.collectPersistentTerms(rightPersistentTerms);
        if (rightPersistentTerms.size() != 1)
            persistentTerms.add(new Dot(left, new ExpressionArray(rightPersistentTerms), outerJoin));
        else if (rightPersistentTerms.get(0) == right)
            persistentTerms.add(this);
        else
            persistentTerms.add(new Dot(left, rightPersistentTerms.get(0), outerJoin));
    }
}
