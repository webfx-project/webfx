package webfx.framework.shared.expression.sqlcompiler.terms;

import webfx.framework.shared.expression.terms.Exists;
import webfx.framework.shared.expression.terms.SelectExpression;

/**
 * @author Bruno Salmon
 */
public final class SelectExpressionSqlCompiler extends AbstractTermSqlCompiler<SelectExpression> {

    public SelectExpressionSqlCompiler() {
        super(SelectExpression.class, Exists.class);
    }

    @Override
    public void compileExpressionToSql(SelectExpression e, Options o) {
        String leftBracket = "(";
        if (e instanceof Exists)
            leftBracket = "exists(";
        StringBuilder clauseBuilder = o.build.prepareAppend(o).append(leftBracket);
        compileSelect(e.getSelect(), o);
        clauseBuilder.append(')');
    }
}
