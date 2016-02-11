package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Alias;
import naga.core.orm.expression.term.As;
import naga.core.orm.expressionparser.expressionbuilder.ReferenceResolver;

/**
 * @author Bruno Salmon
 */
public class AsBuilder extends UnaryExpressionBuilder implements ReferenceResolver {
    public String alias;

    public AsBuilder(ExpressionBuilder operand, String alias) {
        super(operand);
        this.alias = alias;
    }

    @Override
    protected As newUnaryOperation(Expression operand) {
        return new As(operand, alias);
    }

    @Override
    public Expression resolveReference(String name) {
        return name.equals(alias) ? new Alias(alias, operand.build().getType()) : null;
    }
}
