package naga.framework.expression.terms.function;

import naga.commons.util.Arrays;
import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.UnaryExpression;
import naga.commons.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class Call<T> extends UnaryExpression<T> {

    private final String functionName;
    private final Function function;

    public Call(String functionName, Expression argument) {
        super(argument);
        this.functionName = functionName;
        function = Function.getFunction(functionName);
        if (function == null)
            throw new IllegalArgumentException("Unknown function: " + functionName);
        /*
        if ("readOnly".equals(functionName))
            label = PropertyNameExpression.getTopRightExpression(argument).getLabel();*/
    }

    public String getFunctionName() {
        return functionName;
    }

    public Function getFunction() {
        return function;
    }

    @Override
    public Type getType() {
        Type type = function.getReturnType();
        if (type == null) { // type = null means the function returns the same type as the argument (ex: sum function)
            Expression operand = getOperand(); // General case: the type to return is the type of the operand
            if (operand instanceof ExpressionArray) { // Particular case: multiple arguments (ex: coalesce(ar1, arg2))
                Expression[] arguments = ((ExpressionArray) operand).getExpressions();
                if (!Arrays.isEmpty(arguments))
                    operand = arguments[0]; // we return the type of the first argument
            }
            type = operand.getType();
        }
        return type;
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        if (function.isEvaluable())
            return function.evaluate(operand == null ? null : operand.evaluate(domainObject, dataReader), dataReader);
        return dataReader.getDomainFieldValue(domainObject, this);
        //return ThreadContext.getContext().getWorkingData().getValue(id, this /* RawData will return stored value where fieldId = this.getId() = functionName */);
    }

    @Override
    public int getPrecedenceLevel() {
        return function.getPrecedenceLevel();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append(functionName).append('(');
        if (operand != null)
            operand.toString(sb);
        return sb.append(')');
    }

    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        if (!function.isEvaluable())
            persistentTerms.add(this); // assuming the function is sql expressible
        else if (operand != null)
            operand.collectPersistentTerms(persistentTerms);
    }

}
