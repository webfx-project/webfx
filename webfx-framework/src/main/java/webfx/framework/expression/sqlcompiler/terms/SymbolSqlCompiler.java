package webfx.framework.expression.sqlcompiler.terms;

import webfx.framework.orm.domainmodel.DomainField;
import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Dot;
import webfx.framework.expression.terms.Symbol;
import webfx.framework.expression.sqlcompiler.sql.SqlClause;
import webfx.type.Types;
import webfx.framework.orm.domainmodel.FieldsGroup;

/**
 * @author Bruno Salmon
 */
public class SymbolSqlCompiler extends AbstractTermSqlCompiler<Symbol> {

    public SymbolSqlCompiler() {
        super(Symbol.class, DomainField.class, FieldsGroup.class);
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
                o.build.addColumnInClause(o.build.getClassAlias(termDomainClass, o.modelReader), o.modelReader.getSymbolSqlColumnName(termDomainClass, e), e, o.modelReader.getSymbolForeignDomainClass(termDomainClass, e, false), o.clause, o.separator, o.grouped, Types.isBooleanType(e.getType()), o.generateQueryMapping);
            else
                compileChildExpressionToSql(new Dot(e, foreignField, true), o);
        }
    }
}
