package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.term.Constant;

/**
 * @author Bruno Salmon
 */
public class ConstantBuilder extends ExpressionBuilder {

    public Object constantValue;

    public ConstantBuilder(Object constantValue) {
        this.constantValue = constantValue;
    }

    @Override
    public Constant build() {
        return Constant.newConstant(constantValue);
    }
}
