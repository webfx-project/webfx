package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.term.Exists;
import naga.core.orm.expression.term.SelectExpression;

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
