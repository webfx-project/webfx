package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.terms.Constant;

/**
 * @author Bruno Salmon
 */
public final class ConstantBuilder extends ExpressionBuilder {

    public final Object constantValue;

    public ConstantBuilder(Object constantValue) {
        this.constantValue = constantValue;
    }

    @Override
    public Constant build() {
        return Constant.newConstant(constantValue);
    }
}
