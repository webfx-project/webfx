package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.lci.DomainWriter;
import webfx.framework.shared.orm.expression.terms.function.Call;
import webfx.framework.shared.orm.expression.terms.function.Function;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class Dot<T> extends BinaryExpression<T> {

    private final boolean outerJoin;
    private final boolean readLeftKey;

    public Dot(Expression<T> left, Expression<?> right) {
        this(left, right, false);
    }

    public Dot(Expression<T> left, Expression<?> right, boolean outerJoin) {
        this(left, right, outerJoin, true);
    }

    public Dot(Expression<T> left, Expression<?> right, boolean outerJoin, boolean readLeftKey) {
        // TODO Avoid this false right cast by extending (new) PipeExpression<T1, T2> instead of BinaryExpression<T>
        super(left, outerJoin ? ".." : ".", (Expression<T>) right, 8);
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
    public Expression<?> getForwardingTypeExpression() {
        return right;
    }

    @Override
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        Object leftValue = left.evaluate(domainObject, domainReader);
        if (leftValue == null)
            return null;
        T rightData = domainReader.getDomainObjectFromId(leftValue, domainObject);
        return right.evaluate(rightData, domainReader);
    }

    @Override
    public Object evaluate(Object leftValue, Object rightValue, DomainReader<T> domainReader) {
        return null; // never called due to above evaluate method override
    }

    @Override
    public void setValue(T domainObject, Object value, DomainWriter<T> dataWriter) {
        Object leftValue = left.evaluate(domainObject, dataWriter);
        if (leftValue != null) {
            T rightData = dataWriter.getDomainObjectFromId(leftValue, domainObject);
            right.setValue(rightData, value, dataWriter);
        }
    }

    @Override
    public void collect(CollectOptions options) {
        left.collect(options);
        if (!options.factorizeLeftDot())
            right.collect(options);
        else {
            CollectOptions rightOptions = CollectOptions.sameButEmpty(options);
            right.collect(rightOptions);
            List<Expression<T>> rightTerms = rightOptions.getCollectedTerms();
            if (!rightTerms.isEmpty()) {
                Dot<T> persistentDot;
                if (rightTerms.size() != 1) {
                    persistentDot = new Dot<>(left, new ExpressionArray<>(rightTerms), outerJoin);
                }
                else if (rightTerms.get(0) == right) {
                    persistentDot = this;
                }
                else {
                    persistentDot = new Dot<>(left, rightTerms.get(0), outerJoin);
                }
                Expression<T> expandLeft = persistentDot.expandLeft();
                if (expandLeft == persistentDot) {
                    options.addTerm(persistentDot);
                }
                else {
                    expandLeft.collect(options);
                }
            }
        }
    }

    public Expression<T> expandLeft() {
        if (left instanceof Call) {
            Call<T> call = (Call<T>) this.left;
            Function<T> function = call.getFunction();
            if (function.isIdentity())
                return new Call<>(function.getName(), new Dot<>(call.getOperand(), getRight(), isOuterJoin()).expandLeft(), call.getOrderBy());
        }
        if (left instanceof Dot) {
            Dot<T> leftDot = (Dot<T>) left;
            return new Dot<>(leftDot.getLeft(), new Dot<>(leftDot.getRight(), getRight(), isOuterJoin()), leftDot.isOuterJoin()).expandLeft();
        }
        Expression<?> leftForwardingTypeExpression = left.getForwardingTypeExpression();
        if (leftForwardingTypeExpression == left)
            return this;
        if (leftForwardingTypeExpression instanceof Dot) {
            Dot<T> leftDot = (Dot<T>) leftForwardingTypeExpression;
            return new Dot<>(leftDot.getLeft(), new Dot<>(leftDot.getRight(), getRight(), isOuterJoin()), leftDot.isOuterJoin()).expandLeft();
        }
        if (leftForwardingTypeExpression instanceof TernaryExpression) {
            TernaryExpression<T> leftTernaryExpression = (TernaryExpression<T>) leftForwardingTypeExpression;
            return new TernaryExpression<T>(leftTernaryExpression.getQuestion(), new Dot<>(leftTernaryExpression.getYes(), getRight(), isOuterJoin()).expandLeft(), new Dot(leftTernaryExpression.getNo(), getRight(), isOuterJoin()).expandLeft());
        }
        return this;
    }
}
