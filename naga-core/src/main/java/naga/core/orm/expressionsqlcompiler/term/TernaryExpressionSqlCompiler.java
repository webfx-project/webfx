package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.term.TernaryExpression;

/**
 * @author Bruno Salmon
 */
public class TernaryExpressionSqlCompiler extends AbstractTermSqlCompiler<TernaryExpression> {

    public TernaryExpressionSqlCompiler() {
        super(TernaryExpression.class);
    }

    @Override
    public void compileExpressionToSql(TernaryExpression e, Options o) {
        StringBuilder sb = o.build.prepareAppend(o);
        o = o.changeSeparatorGenerateQueryMapping(null, false); // no separator before then, else, end and no generateQueryMapping within the ternary expression
        sb.append("case when ");
        compileChildExpressionToSql(e.getQuestion(), o.changeReadForeignFields(false));
        sb.append(" then ");
        compileChildExpressionToSql(e.getYes(), o);
        sb.append(" else ");
        compileChildExpressionToSql(e.getNo(), o);
        sb.append(" end");
    }
}
