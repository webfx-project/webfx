package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Dot;
import naga.core.orm.expression.term.Symbol;
import naga.core.orm.expressionsqlcompiler.sql.SqlClause;
import naga.core.type.Types;

/**
 * @author Bruno Salmon
 */
public class SymbolSqlCompiler extends AbstractTermSqlCompiler<Symbol> {

    public SymbolSqlCompiler() {
        super(Symbol.class);
    }

    @Override
    public void compileExpressionToSql(Symbol e, Options o) {
        if (e.getExpression() != null)
            compileChildExpressionToSql(e.getExpression(), o);
        else {
            Expression foreignField = null;
            Object termDomainClass = o.build.getCompilingClass();
            if (o.readForeignFields && o.clause == SqlClause.SELECT) {
                Object foreignClass = o.modelReader.getSymbolForeignDomainClass(termDomainClass, e, false);
                if (foreignClass != null /* && build.getJoinMapping() == null  to avoid infinite recursion, see item.icon*/)
                    foreignField = o.modelReader.getDomainClassDefaultForeignFields(foreignClass);
            }
            if (foreignField == null)
                o.build.addColumnInClause(o.build.getClassAlias(termDomainClass, o.modelReader), o.modelReader.getSymbolSqlColumnName(termDomainClass, e), e, null, o.clause, o.separator, o.grouped, Types.isBooleanType(e.getType()), o.generateQueryMapping);
            else
                compileChildExpressionToSql(new Dot(e, foreignField, true), o);
        }
    }
}
