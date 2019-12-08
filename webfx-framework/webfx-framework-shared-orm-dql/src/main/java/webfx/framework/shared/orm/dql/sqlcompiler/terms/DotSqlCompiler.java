package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.dql.sqlcompiler.mapping.QueryColumnToEntityFieldMapping;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlClause;
import webfx.framework.shared.orm.expression.terms.Alias;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.framework.shared.orm.expression.terms.Dot;
import webfx.framework.shared.orm.expression.terms.Symbol;
import webfx.framework.shared.orm.expression.terms.function.ArgumentAlias;

/**
 * @author Bruno Salmon
 */
public final class DotSqlCompiler extends AbstractTermSqlCompiler<Dot> {

    public DotSqlCompiler() {
        super(Dot.class);
    }

    @Override
    public void compileExpressionToSql(Dot dot, Options o) {
/*
        Expression expandLeft = dot.expandLeft();
        if (expandLeft == dot || expandLeft instanceof Dot)
            dot = (Dot) expandLeft;
        else {
            compileChildExpressionToSql(expandLeft, o);
            return;
        }
*/
        Expression left = dot.getLeft();
        Object leftClass = o.build.getCompilingClass();
        String asAlias = null;
        if (left instanceof As) {
            As as = (As) left;
            left = as.getOperand();
            asAlias = as.getAlias();
        }
        Object rightClass = o.modelReader.getSymbolForeignDomainClass(leftClass, left, true); // was e.getType().getForeignClass();
        final String leftTableAlias = o.build.getCompilingTableAlias();
        final String leftSql;
        final String rightTableAlias;
        if (left instanceof ArgumentAlias)
            left = (Expression) ((ArgumentAlias) left).getArgument();
        String leftSqlColumnName = o.modelReader.getSymbolSqlColumnName(leftClass, left);
        if (leftSqlColumnName != null) { // typically a persistent field
            leftSql = leftSqlColumnName;
            rightTableAlias = o.build.addJoinCondition(leftTableAlias, leftSql, asAlias, o.modelReader.getDomainClassSqlTableName(rightClass), o.modelReader.getDomainClassPrimaryKeySqlColumnName(rightClass), dot.isOuterJoin() || o.clause == SqlClause.SELECT);
        } else if (left instanceof Alias) {
            leftSql = null;
            Alias alias = (Alias) left;
            rightClass = alias.getDomainClass();
            rightTableAlias = alias.getName();
        } else // should never occur
            leftSql = rightTableAlias = null;
        QueryColumnToEntityFieldMapping leftJoinMapping = null;
        if (o.clause == SqlClause.SELECT && leftSql != null && dot.isReadLeftKey() && o.readForeignFields) // lecture de la clé étrangère pour pouvoir faire la jointure en mémoire
            leftJoinMapping = o.build.addColumnInClause(leftTableAlias, leftSql, left, rightClass, o.clause, o.separator, o.grouped, false, o.generateQueryMapping);
        o.build.setCompilingClass(rightClass);
        o.build.setCompilingTableAlias(rightTableAlias);
        QueryColumnToEntityFieldMapping oldLeftJoinMapping = o.build.getLeftJoinMapping();
        o.build.setLeftJoinMapping(leftJoinMapping);
        Expression right = dot.getRight();
        if (o.clause == SqlClause.SELECT && (!(right instanceof Symbol) || ((Symbol) right).getExpression() != null))
            compileExpressionPersistentTermsToSql(right, o);
        else
            compileChildExpressionToSql(right, o);
        o.build.setLeftJoinMapping(oldLeftJoinMapping);
        o.build.setCompilingClass(leftClass);
        o.build.setCompilingTableAlias(leftTableAlias);
    }
}
