package naga.core.orm.expression.term.function;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataReader;
import naga.core.orm.expression.term.UnaryExpression;
import naga.core.type.Type;

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
        Type type = function.getReturnType(); // type = null means return same type as argument (ex: sum function)
        return type != null ? type : super.getType();
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
