package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;

/**
 * @author Bruno Salmon
 */
public final class ExpressionArraySqlCompiler extends AbstractTermSqlCompiler<ExpressionArray> {

    public ExpressionArraySqlCompiler() {
        super(ExpressionArray.class);
    }

    @Override
    public void compileExpressionToSql(ExpressionArray e, Options o) {
        for (Expression child : e.getExpressions())
            compileChildExpressionToSql(child, o);
    }
}
