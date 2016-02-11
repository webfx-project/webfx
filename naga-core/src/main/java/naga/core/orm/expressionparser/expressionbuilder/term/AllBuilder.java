package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.All;

/**
 * @author Bruno Salmon
 */
public class AllBuilder extends BooleanExpressionBuilder {
    String operator;

    public AllBuilder(ExpressionBuilder left, String operator, ExpressionBuilder right) {
        super(left, right);
        this.operator = operator;
    }

    @Override
    protected All newBinaryOperation(Expression left, Expression right) {
        return new All(left, operator, right);
    }
}
