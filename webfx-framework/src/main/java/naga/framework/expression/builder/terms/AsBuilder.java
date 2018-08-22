package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Alias;
import naga.framework.expression.terms.As;
import naga.framework.expression.builder.ReferenceResolver;

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
