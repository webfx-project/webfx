package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.framework.shared.orm.expression.terms.Alias;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.framework.shared.orm.expression.terms.Symbol;

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
        Expression build = operand.build();
        return name.equals(alias) ? new Alias(alias, build.getType(), getModelReader().getSymbolForeignDomainClass(buildingClass, (Symbol) build)): null;
    }
}
