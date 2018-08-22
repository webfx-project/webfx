package naga.framework.expression.sqlcompiler.terms;

import naga.framework.expression.terms.Exists;
import naga.framework.expression.terms.SelectExpression;

/**
 * @author Bruno Salmon
 */
public class SelectExpressionSqlCompiler extends AbstractTermSqlCompiler<SelectExpression> {

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
