package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Alias;
import webfx.framework.expression.terms.As;
import webfx.framework.expression.builder.ReferenceResolver;

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
