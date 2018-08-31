package webfx.framework.expression.sqlcompiler.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.ExpressionArray;

/**
 * @author Bruno Salmon
 */
public class ExpressionArraySqlCompiler extends AbstractTermSqlCompiler<ExpressionArray> {

    public ExpressionArraySqlCompiler() {
        super(ExpressionArray.class);
    }

    @Override
    public void compileExpressionToSql(ExpressionArray e, Options o) {
        for (Expression child : e.getExpressions())
            compileChildExpressionToSql(child, o);
    }
}
