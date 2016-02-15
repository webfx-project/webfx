package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Dot;
import naga.core.orm.expression.term.function.Call;

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
