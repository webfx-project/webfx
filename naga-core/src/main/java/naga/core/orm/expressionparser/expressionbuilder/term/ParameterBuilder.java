package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Parameter;

/**
 * @author Bruno Salmon
 */
public class ParameterBuilder extends ExpressionBuilder {

    public String name;
    public ExpressionBuilder rightDot;

    public ParameterBuilder() {
    }

    public ParameterBuilder(String name) {
        this.name = name;
    }

    public ParameterBuilder(String name, ExpressionBuilder rightDot) {
        this.name = name;
        this.rightDot = rightDot;
    }

    @Override
    public Expression build() {
        if (name == null)
            return Parameter.UNNAMED_PARAMETER;
        Expression rd = null;
        /*
        if (rightDot != null) {
            rightDot.buildingClass = buildingClass.getDataModel().getParameterClass(name);
            if (rightDot.buildingClass == null && name.startsWith("this"))
                rightDot.buildingClass = buildingClass.getDataModel().getClass(name.substring(4));
            if (rightDot.buildingClass != null)
                rd = rightDot.build();
            else // might happen on server side, but we don't need rightDot in that case since we have the value
                name += '.' + rightDot.toString(); // just keep a trace of it in the name
        }
        */
        return new Parameter(name, rd);
    }
}
