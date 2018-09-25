package webfx.framework.expression.sqlcompiler.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.sqlcompiler.ExpressionSqlCompiler;
import webfx.framework.expression.sqlcompiler.sql.SqlClause;
import webfx.framework.expression.terms.Dot;
import webfx.framework.expression.terms.function.Call;
import webfx.framework.expression.terms.function.Function;
import webfx.framework.expression.terms.function.InlineFunction;

/**
 * @author Bruno Salmon
 */
public final class CallSqlCompiler extends AbstractTermSqlCompiler<Call> {

    public CallSqlCompiler() {
        super(Call.class);
    }

    @Override
    public void compileExpressionToSql(Call call, Options o) {
        Expression arg = call.getOperand();
        if (arg instanceof Dot) {
            Dot dot = (Dot) arg;
            compileChildExpressionToSql(new Dot(dot.getLeft(), new Call(call.getFunctionName(), dot.getRight(), call.getOrderBy()), dot.isOuterJoin(), false), o);
        } else {
            Function f = call.getFunction();
            if (f instanceof InlineFunction) {
                InlineFunction inline = (InlineFunction) f;
                if (o.clause == SqlClause.SELECT)
                    compileExpressionPersistentTermsToSql(arg, o);
                else
                    try {
                        inline.pushArguments(arg);
                        compileChildExpressionToSql(inline.getBody(), o);
                    } finally {
                        inline.popArguments();
                    }
            } else {
                StringBuilder sb;
                String name = ExpressionSqlCompiler.toSqlString(f.getName()); // Ex: AbcNames transformed to abc_names
                if (o.generateQueryMapping) {
                    o.build.addColumnInClause(null, name, name, null, o.clause, o.separator, false, false, true);
                    sb = o.build.prepareAppend(o.clause, "");
                } else
                    sb = o.build.prepareAppend(o).append(name);
                if (!f.isKeyword()) {
                    sb.append('(');
                    if (arg != null)
                        compileChildExpressionToSql(arg, o.changeSeparatorGroupedGenerateQueryMapping(",", false, false));
                    if (call.getOrderBy() != null) {
                        sb.append(" order by ");
                        compileChildExpressionToSql(call.getOrderBy(), o.changeSeparatorGroupedGenerateQueryMapping(",", false, false));
                    }
                    sb.append(')');
                }
            }
        }
    }
}
