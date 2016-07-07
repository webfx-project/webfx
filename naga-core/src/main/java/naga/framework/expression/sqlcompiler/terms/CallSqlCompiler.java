package naga.framework.expression.sqlcompiler.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Dot;
import naga.framework.expression.terms.function.Call;

/**
 * @author Bruno Salmon
 */
public class CallSqlCompiler extends AbstractTermSqlCompiler<Call> {

    public CallSqlCompiler() {
        super(Call.class);
    }

    @Override
    public void compileExpressionToSql(Call e, Options o) {
        Expression arg = e.getOperand();
        if (arg instanceof Dot) {
            Dot dot = (Dot) arg;
            compileChildExpressionToSql(new Dot(dot.getLeft(), new Call(e.getFunctionName(), dot.getRight()), dot.isOuterJoin(), false), o);
        } else
            compileFunctionToSql(e.getFunction(), arg, o);
    }
}
