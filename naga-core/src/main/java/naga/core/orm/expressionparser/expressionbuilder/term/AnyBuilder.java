package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Any;

/**
 * @author Bruno Salmon
 */
public class AnyBuilder extends BooleanExpressionBuilder {
    String operator;

    public AnyBuilder(ExpressionBuilder left, String operator, ExpressionBuilder right) {
        super(left, right);
        this.operator = operator;
    }

    @Override
    protected Any newBinaryOperation(Expression left, Expression right) {
        return new Any(left, operator, right);
    }
}
