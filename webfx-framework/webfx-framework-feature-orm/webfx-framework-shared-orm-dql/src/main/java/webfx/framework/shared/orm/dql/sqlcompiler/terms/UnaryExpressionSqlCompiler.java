package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.terms.Array;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.framework.shared.orm.expression.terms.Not;
import webfx.framework.shared.orm.expression.terms.UnaryExpression;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlClause;

/**
 * @author Bruno Salmon
 */
public final class UnaryExpressionSqlCompiler extends AbstractTermSqlCompiler<UnaryExpression> {

    public UnaryExpressionSqlCompiler() {
        super(Array.class, As.class, Not.class);
    }

    @Override
    public void compileExpressionToSql(UnaryExpression e, Options o) {
        if (e instanceof As) {
            compileChildExpressionToSql(e.getOperand(), o.changeGenerateQueryMapping(false));
            if (o.clause == SqlClause.SELECT) {
                String alias = ((As) e).getAlias();
                o.build.addColumnInClause(null, alias, alias, null, o.clause, " as ", false, false, o.generateQueryMapping);
            }
        } else {
            String left, right;
            if (e instanceof Array) {
                left = "array[";
                right = "]";
            } else if (e instanceof Not) {
                left = "not (";
                right = ")";
            } else
                throw new IllegalArgumentException();
            StringBuilder clauseBuilder = o.build.prepareAppend(o).append(left);
            compileChildExpressionToSql(e.getOperand(), o);
            clauseBuilder.append(right);
        }
    }
}
