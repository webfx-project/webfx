package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Select;
import naga.core.orm.expression.term.function.Function;
import naga.core.orm.expression.term.function.InlineFunction;
import naga.core.orm.expressionsqlcompiler.ExpressionSqlCompiler;
import naga.core.orm.expressionsqlcompiler.sql.SqlClause;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractTermSqlCompiler<E extends Expression> {

    private final Class<? extends Expression>[] supportedTermClasses;

    public AbstractTermSqlCompiler(Class<? extends Expression>... supportedTermClasses) {
        this.supportedTermClasses = supportedTermClasses;
    }

    public Class<? extends Expression>[] getSupportedTermClasses() {
        return supportedTermClasses;
    }

    public abstract void compileExpressionToSql(E e, Options o);

    protected void compileChildExpressionToSql(Expression e, Options o) {
        ExpressionSqlCompiler.compileExpression(e, o);
    }

    protected void compileExpressionPersistentTermsToSql(Expression e, Options o) {
        List<Expression> persistentTerms = new ArrayList<>();
        e.collectPersistentTerms(persistentTerms);
        for (Expression term : persistentTerms)
            compileChildExpressionToSql(term, o);
    }

    protected void compileSelect(Select select, Options o) {
        ExpressionSqlCompiler.compileSelect(select, o);
    }

    void compileFunctionToSql(Function f, Expression arg, Options o) {
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
            return;
        }
        StringBuilder sb;
        String name = f.getName();
        if (o.generateQueryMapping) {
            o.build.addColumnInClause(null, name, name, null, o.clause, o.separator, false, false, true);
            sb = o.build.prepareAppend(o.clause, "").append('(');
        } else
            sb = o.build.prepareAppend(o).append(name).append('(');
        if (arg != null)
            compileChildExpressionToSql(arg, o.changeSeparatorGroupedGenerateQueryMapping(",", false, false));
        sb.append(')');
    }

}
