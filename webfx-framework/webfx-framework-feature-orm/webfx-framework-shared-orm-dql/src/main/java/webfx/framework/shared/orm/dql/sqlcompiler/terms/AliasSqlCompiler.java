package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Alias;
import webfx.framework.shared.orm.expression.terms.function.ArgumentAlias;

/**
 * @author Bruno Salmon
 */
public final class AliasSqlCompiler extends AbstractTermSqlCompiler<Alias> {

    public AliasSqlCompiler() {
        super(Alias.class, ArgumentAlias.class);
    }

    @Override
    public void compileExpressionToSql(Alias e, Options o) {
        if (e instanceof ArgumentAlias) /***  called during inline function sql compilation ***/
            compileChildExpressionToSql((Expression) ((ArgumentAlias) e).getArgument(), o);
        else // Standard alias, called only when alone (not followed by dot since Dot manages this case), so refers implicitly to id in that case
            o.build.prepareAppend(o).append(o.build.getSqlAlias(e.getName())).append('.').append(o.modelReader.getDomainClassPrimaryKeySqlColumnName(e.getDomainClass()));
    }
}
