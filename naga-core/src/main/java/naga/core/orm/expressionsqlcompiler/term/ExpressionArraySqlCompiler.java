package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.ExpressionArray;

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
