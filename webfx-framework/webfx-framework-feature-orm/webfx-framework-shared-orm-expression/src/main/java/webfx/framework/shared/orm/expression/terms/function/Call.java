package webfx.framework.shared.orm.expression.terms.function;

import webfx.extras.type.Type;
import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.expression.terms.Ordered;
import webfx.framework.shared.orm.expression.terms.UnaryExpression;
import webfx.platform.shared.util.Arrays;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class Call<T> extends UnaryExpression<T> {

    private final String functionName;
    private final Function<T> function;
    private final ExpressionArray<T> orderBy; // used only for aggregate functions

    public Call(String functionName, Expression<T> argument) {
        this(functionName, argument, null);
    }

    public Call(String functionName, Expression<T> argument, ExpressionArray<T> orderBy) {
        super(argument);
        this.functionName = functionName;
        function = Function.getFunction(functionName);
        if (function == null)
            throw new IllegalArgumentException("Unknown function: " + functionName);
        this.orderBy = orderBy;
        /*
        if ("readOnly".equals(functionName))
            label = PropertyNameExpression.getTopRightExpression(argument).getLabel();*/
    }

    public String getFunctionName() {
        return functionName;
    }

    public Function<T> getFunction() {
        return function;
    }

    public ExpressionArray<T> getOrderBy() {
        return orderBy;
    }

    @Override
    public Expression<?> getForwardingTypeExpression() {
        Type type = function.getReturnType();
        if (type != null)
            return this;
        Expression<T> operand = getOperand(); // General case: the type to return is the type of the operand
        if (operand instanceof ExpressionArray) { // Particular case: multiple arguments (ex: coalesce(ar1, arg2))
            Expression<T>[] arguments = ((ExpressionArray<T>) operand).getExpressions();
            if (!Arrays.isEmpty(arguments))
                operand = arguments[0]; // we return the type of the first argument
        }
        return operand != null ? operand : this;
    }

    @Override
    public Type getType() {
        Expression forwardingTypeExpression = getForwardingTypeExpression();
        return forwardingTypeExpression == this ? function.getReturnType() : forwardingTypeExpression.getType();
    }

    @Override
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        if (function.isEvaluable()) {
            if (!(function instanceof AggregateFunction))
                return function.evaluate(operand == null ? null : (T) operand.evaluate(domainObject, domainReader), domainReader);
            Object primaryKey = domainReader.prepareValueBeforeTypeConversion(domainObject, null);
            if (primaryKey instanceof AggregateKey) {
                AggregateKey<T> aggregateKey = (AggregateKey<T>) primaryKey;
                if (orderBy != null)
                    orderBy(aggregateKey.getAggregates(), domainReader, orderBy);
                return ((AggregateFunction<T>) function).evaluateOnAggregates(domainObject, aggregateKey.getAggregates().toArray(), operand, domainReader);
            }
        }
        // When the function is not evaluable, it might have been already evaluated during the query and the result stored in a field
        return domainReader.getDomainFieldValue(domainObject, functionName); // using function name as field (ex: count(1) -> field = count)
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public int getPrecedenceLevel() {
        return function.getPrecedenceLevel();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append(functionName);
        if (!function.isKeyword()) {
            sb.append('(');
            if (operand != null)
                sb = operand.toString(sb);
            if (orderBy != null)
                sb = orderBy.toString(sb.append(" order by "));
            sb.append(')');
        }
        return sb;
    }

    @Override
    public void collect(CollectOptions options) {
        if (!options.traverseSqlExpressible() && function.isSqlExpressible())
            options.addTerm(this);
        else if (operand != null)
            operand.collect(options);
    }

    public static <T> List<T> orderBy(List<T> list, DomainReader<T> domainReader, Expression<T>... orderExpressions) {
        if (orderExpressions.length == 1 && orderExpressions[0] instanceof ExpressionArray)
            orderBy(list, domainReader, ((ExpressionArray<T>) orderExpressions[0]).getExpressions());
        else list.sort((e1, e2) -> {
            if (e1 == e2)
                return 0;
            if (e1 == null)
                return 1;
            if (e2 == null)
                return -1;
            for (Expression<T> orderExpression : orderExpressions) {
                Object o1 = orderExpression.evaluate(e1, domainReader);
                Object o2 = orderExpression.evaluate(e2, domainReader);
                Ordered<T> ordered = orderExpression instanceof Ordered ? (Ordered<T>) orderExpression : null;
                int result;
                if (o1 == o2)
                    result = 0;
                else if (!(o1 instanceof Comparable))
                    result = ordered == null || ordered.isNullsLast() ? 1 : -1;
                else if (!(o2 instanceof Comparable))
                    result = ordered == null || ordered.isNullsLast() ? -1 : 1;
                else {
                    result = ((Comparable) o1).compareTo(o2);
                    if (ordered != null && ordered.isDescending())
                        result = -result;
                }
                if (result != 0)
                    return result;
            }
            return 0;
        });
        return list;
    }
}
