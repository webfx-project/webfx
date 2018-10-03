package webfx.framework.shared.expression.builder.terms;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.Alias;
import webfx.framework.shared.expression.terms.As;
import webfx.framework.shared.expression.builder.ReferenceResolver;

/**
 * @author Bruno Salmon
 */
public final class AsBuilder extends UnaryExpressionBuilder implements ReferenceResolver {
    public final String alias;

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
