package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.LessThan;

/**
 * @author Bruno Salmon
 */
public class LessThanBuilder extends BinaryBooleanExpressionBuilder {

    public LessThanBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected LessThan newBinaryOperation(Expression left, Expression right) {
        return new LessThan(left, right);
    }
}
