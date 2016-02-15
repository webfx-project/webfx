package naga.core.orm.expressionsqlcompiler.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Alias;
import naga.core.orm.expression.term.As;
import naga.core.orm.expression.term.Dot;
import naga.core.orm.expression.term.Symbol;
import naga.core.orm.expression.term.function.ArgumentAlias;
import naga.core.orm.expressionsqlcompiler.sql.SqlClause;
import naga.core.orm.mapping.query.SqlColumnToEntityFieldMapping;

/**
 * @author Bruno Salmon
 */
public class DotSqlCompiler extends AbstractTermSqlCompiler<Dot> {

    public DotSqlCompiler() {
        super(Dot.class);
    }

    @Override
    public void compileExpressionToSql(Dot e, Options o) {
        Expression left = e.getLeft();
        Object leftClass = o.build.getCompilingClass();
        Object rightClass = o.modelReader.getSymbolForeignDomainClass(leftClass, left, true); // was e.getType().getForeignClass();
        final String leftTableAlias = o.build.getCompilingTableAlias();
        final String leftSql;
        final String rightTableAlias;
        String asAlias = null;
        if (left instanceof ArgumentAlias)
            left = (Expression) ((ArgumentAlias) left).getArgument();
        if (left instanceof As) {
            As as = (As) left;
            left = as.getOperand();
            asAlias = as.getAlias();
        }
        String leftSqlColumnName = o.modelReader.getSymbolSqlColumnName(leftClass, left);
        if (leftSqlColumnName != null) { // typically a persistent field
            leftSql = leftSqlColumnName;
            rightTableAlias = o.build.addJoinCondition(leftTableAlias, leftSql, asAlias, o.modelReader.getDomainClassSqlTableName(rightClass), o.modelReader.getDomainClassPrimaryKeySqlColumnName(rightClass), e.isOuterJoin() || o.clause == SqlClause.SELECT);
        } else if (left instanceof Alias) {
            leftSql = null;
            rightTableAlias = ((Alias) left).getName();
        } else // should never occur
            leftSql = rightTableAlias = null;
        SqlColumnToEntityFieldMapping leftJoinMapping = null;
        if (o.clause == SqlClause.SELECT && leftSql != null && e.isReadLeftKey() && o.readForeignFields) // lecture de la clé étrangère pour pouvoir faire la jointure en mémoire
            leftJoinMapping = o.build.addColumnInClause(leftTableAlias, leftSql, left, rightClass, o.clause, o.separator, o.grouped, false, o.generateQueryMapping);
        o.build.setCompilingClass(rightClass);
        o.build.setCompilingTableAlias(rightTableAlias);
        SqlColumnToEntityFieldMapping oldLeftJoinMapping = o.build.getLeftJoinMapping();
        o.build.setLeftJoinMapping(leftJoinMapping);
        Expression right = e.getRight();
        if (o.clause == SqlClause.SELECT && (!(right instanceof Symbol) || ((Symbol) right).getExpression() != null))
            compileExpressionPersistentTermsToSql(right, o);
        else
            compileChildExpressionToSql(right, o);
        o.build.setLeftJoinMapping(oldLeftJoinMapping);
        o.build.setCompilingClass(leftClass);
        o.build.setCompilingTableAlias(leftTableAlias);
    }
}
