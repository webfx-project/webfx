package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.And;

/**
 * @author Bruno Salmon
 */
public class AndBuilder extends BinaryExpressionBuilder {

    public AndBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected And newBinaryOperation(Expression left, Expression right) {
        return new And(left, right);
    }
}
