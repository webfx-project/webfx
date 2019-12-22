package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.terms.IdExpression;
import webfx.extras.type.Types;

/**
 * @author Bruno Salmon
 */
public final class IdSqlCompiler extends AbstractTermSqlCompiler<IdExpression> {

    public IdSqlCompiler() {
        super(IdExpression.class);
    }

    @Override
    public void compileExpressionToSql(IdExpression e, Options o) {
        Object compilingClass = o.build.getCompilingClass();
        o.build.addColumnInClause(o.build.getClassAlias(compilingClass, o.modelReader), o.modelReader.getDomainClassPrimaryKeySqlColumnName(compilingClass), e, null, o.clause, o.separator, o.grouped, Types.isBooleanType(e.getType()), o.generateQueryMapping);
    }
}
