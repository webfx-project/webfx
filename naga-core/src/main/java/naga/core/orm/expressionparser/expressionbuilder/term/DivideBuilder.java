package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Divide;

/**
 * @author Bruno Salmon
 */
public class DivideBuilder extends BinaryExpressionBuilder {

    public DivideBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Divide newBinaryOperation(Expression left, Expression right) {
        return new Divide(left, right);
    }
}
