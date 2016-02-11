package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.function.Call;

/**
 * @author Bruno Salmon
 */
public class CallBuilder extends UnaryExpressionBuilder {

    public String functionName;

    public CallBuilder(String functionName) {
        this(functionName, null);
    }

    public CallBuilder(String functionName, ExpressionBuilder operand) {
        super(operand);
        this.functionName = functionName;
    }

    @Override
    protected Call newUnaryOperation(Expression operand) {
        return new Call(functionName, operand);
    }
}
