package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.term.IDExpression;
import naga.core.type.Types;

/**
 * @author Bruno Salmon
 */
public class IDSqlCompiler extends AbstractTermSqlCompiler<IDExpression> {

    public IDSqlCompiler() {
        super(IDExpression.class);
    }

    @Override
    public void compileExpressionToSql(IDExpression e, Options o) {
        Object compilingClass = o.build.getCompilingClass();
        o.build.addColumnInClause(o.build.getClassAlias(compilingClass, o.modelReader), o.modelReader.getDomainClassPrimaryKeySqlColumnName(compilingClass), e, null, o.clause, o.separator, o.grouped, Types.isBooleanType(e.getType()), o.generateQueryMapping);
    }
}
