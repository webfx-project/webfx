package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ExpressionArrayBuilder extends ExpressionBuilder {
    public List<ExpressionBuilder> expressions = new ArrayList<>();

    public ExpressionArrayBuilder() {
    }

    public ExpressionArrayBuilder(ExpressionBuilder e) {
        expressions.add(e);
    }

    public ExpressionArray build() {
        Expression[] exps = new Expression[expressions.size()];
        for (int i = 0; i < expressions.size(); i++) {
            ExpressionBuilder expb = expressions.get(i);
            expb.buildingClass = buildingClass;
            exps[i] = expb.build();
        }
        return new ExpressionArray(exps);
    }

    public static ExpressionArrayBuilder add(ExpressionBuilder a, ExpressionBuilder b) {
        ExpressionArrayBuilder elb;
        if (a instanceof ExpressionArrayBuilder)
            elb = (ExpressionArrayBuilder) a;
        else
            elb = new ExpressionArrayBuilder(a);
        elb.expressions.add(b);
        return elb;
    }
}
