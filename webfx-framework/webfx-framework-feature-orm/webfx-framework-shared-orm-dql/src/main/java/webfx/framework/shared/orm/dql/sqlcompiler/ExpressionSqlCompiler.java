package webfx.framework.shared.orm.dql.sqlcompiler;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.dql.sqlcompiler.lci.CompilerDomainModelReader;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlBuild;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlClause;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms.DbmsSqlSyntax;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms.HsqlSyntax;
import webfx.framework.shared.orm.dql.sqlcompiler.terms.*;
import webfx.framework.shared.orm.expression.terms.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class ExpressionSqlCompiler {

    private static final Map<Class<? extends Expression>, AbstractTermSqlCompiler<?>> termCompilers = new HashMap<>();

    static {
        // Registering all term compilers (some can actually compile several term classes).
        registerTermCompilers(
                new ExpressionArraySqlCompiler(),   // ExpressionArray
                new BinaryExpressionSqlCompiler(),  // Divide, Minus, Multiply, Plus
                new BooleanExpressionSqlCompiler(), // And, Equals, GreaterThan, GreaterThanOrEquals, In, LessThan, LessThanOrEquals, Like, NotEquals, NotLike, Or, All, Any
                new AliasSqlCompiler(),             // Alias, ArgumentAlias
                new CallSqlCompiler(),              // Call
                new DotSqlCompiler(),               // Dot
                new SelectExpressionSqlCompiler(),  // SelectExpression, Exists
                new TernaryExpressionSqlCompiler(), // TernaryExpression
                new UnaryExpressionSqlCompiler(),   // Array, As, Not
                new OrderedSqlCompiler(),           // Ordered
                new ConstantSqlCompiler(),          // Constant
                new ParameterSqlCompiler(),         // Parameter
                new IdSqlCompiler(),                // IdExpression
                new SymbolSqlCompiler()             // Symbol (then extendable using)
        );
    }

    private static void registerTermCompilers(AbstractTermSqlCompiler<?>... termSqlCompilers) {
        for (AbstractTermSqlCompiler<?> expressionSqlCompiler : termSqlCompilers)
            for (Class<? extends Expression> expressionClass : expressionSqlCompiler.getSupportedTermClasses())
                termCompilers.put(expressionClass, expressionSqlCompiler);
    }

    @SafeVarargs
    public static <T extends Expression> void declareCompilableSubclasses(Class<T> superClass, Class<? extends T>... subclasses) {
        AbstractTermSqlCompiler<?> superCompiler = termCompilers.get(superClass);
        for (Class<? extends T> subclass : subclasses)
            termCompilers.put(subclass, superCompiler);
    }

    @SafeVarargs
    public static void declareSymbolSubclasses(Class<? extends Symbol>... symbolSubclasses) {
        declareCompilableSubclasses(Symbol.class, symbolSubclasses);
    }

    /*** Public entry points ***/

    public static SqlCompiled compileStatement(DqlStatement statement, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader modelReader) {
        if (statement instanceof Insert)
            return compileInsert((Insert) statement, dbmsSyntax, modelReader);
        if (statement instanceof Update)
            return compileUpdate((Update) statement, dbmsSyntax, modelReader);
        if (statement instanceof Delete)
            return compileDelete((Delete) statement, dbmsSyntax, modelReader);
        if (statement instanceof Select)
            return compileSelect((Select) statement, dbmsSyntax, false, false, modelReader);
        return null;
    }

    // Select compilation

    public static SqlCompiled compileSelect(Select select, DbmsSqlSyntax dbmsSyntax, boolean generateQueryMapping, boolean readForeignFields, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = buildSelect(select, dbmsSyntax, generateQueryMapping, readForeignFields, null, null, modelReader);
        return sqlBuild.toSqlCompiled();
    }

    // Insert compilation

    public static SqlCompiled compileInsert(Insert insert, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = buildInsert(insert, dbmsSyntax, modelReader);
        return sqlBuild.toSqlCompiled();
    }

    // Update compilation

    public static SqlCompiled compileUpdate(Update update, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = buildUpdate(update, dbmsSyntax, modelReader);
        return sqlBuild.toSqlCompiled();
    }

    // Delete compilation

    public static SqlCompiled compileDelete(Delete delete, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = buildDelete(delete, dbmsSyntax, modelReader);
        return sqlBuild.toSqlCompiled();
    }


     /*** Private entry points ***/

     // Select compilation

    public static SqlBuild buildSelect(Select select, Options parentOptions) {
        Options o = parentOptions;
        return buildSelect(select, o.build.getDbmsSyntax(), o.generateQueryMapping, o.readForeignFields, o.build, o.clause, o.modelReader);
    }

    public static SqlBuild buildSelect(Select select, DbmsSqlSyntax dbmsSyntax, boolean generateQueryMapping, boolean readForeignFields, SqlBuild parent, SqlClause parentClause, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = createSqlOrderBuild(select, SqlClause.SELECT, dbmsSyntax, parent, modelReader);
        sqlBuild.setDistinct(select.isDistinct());
        boolean grouped = select.getGroupBy() != null;
        if (select.isIncludeIdColumn())
            sqlBuild.addColumnInClause(sqlBuild.getTableAlias(), modelReader.getDomainClassPrimaryKeySqlColumnName(select.getDomainClass()), null, null, SqlClause.SELECT, "", grouped, false, true);
        if (select.getFields() != null)
            compileExpression(select.getFields(), new Options(sqlBuild, SqlClause.SELECT, ", ", grouped, generateQueryMapping, readForeignFields, modelReader));
        if (select.getGroupBy() != null)
            compileExpression(select.getGroupBy(), new Options(sqlBuild, SqlClause.GROUP_BY, ", ", grouped, false, false, modelReader));
        if (select.getHaving() != null)
            compileExpression(select.getHaving(), new Options(sqlBuild, SqlClause.HAVING, ", ", grouped, false, false, modelReader));
        return buildCommonSqlOrder(select, sqlBuild, grouped, dbmsSyntax, parent, parentClause, modelReader);
    }

    private static SqlBuild createSqlOrderBuild(DqlStatement dqlStatement, SqlClause sqlClause, DbmsSqlSyntax dbmsSyntax, SqlBuild parent, CompilerDomainModelReader modelReader) {
        return new SqlBuild(parent, dqlStatement.getDomainClass(), dqlStatement.getDomainClassAlias(), sqlClause, dbmsSyntax, modelReader);
    }

    private static SqlBuild buildCommonSqlOrder(DqlStatement dqlStatement, SqlBuild sqlBuild, boolean grouped, DbmsSqlSyntax dbmsSyntax, SqlBuild parent, SqlClause parentClause, CompilerDomainModelReader modelReader) {
        if (dqlStatement.getWhere() != null)
            compileExpression(dqlStatement.getWhere(), new Options(sqlBuild, SqlClause.WHERE, null, grouped, false, false, modelReader));
        if (dqlStatement.getOrderBy() != null)
            compileExpression(dqlStatement.getOrderBy(), new Options(sqlBuild, SqlClause.ORDER_BY, ", ", grouped, false, false, modelReader));
        if (dqlStatement.getLimit() != null && dbmsSyntax != HsqlSyntax.get()) // temporary fix
            compileExpression(dqlStatement.getLimit(), new Options(sqlBuild, SqlClause.LIMIT, null, grouped, false, false, modelReader));
        if (parent != null)
            parent.prepareAppend(parentClause, null).append(sqlBuild.toSql()); // is it the right way?
        dqlStatement.setCacheable(sqlBuild.isCacheable());
        return sqlBuild;
    }

    // Update compilation

    private static SqlBuild buildInsert(Insert insert, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = createSqlOrderBuild(insert, SqlClause.INSERT, dbmsSyntax, null, modelReader);
        String primaryKeySqlColumnName = modelReader.getDomainClassPrimaryKeySqlColumnName(insert.getDomainClass());
        sqlBuild.setAutoGeneratedKeyColumnNames(new String[]{primaryKeySqlColumnName});
        Options insertOptions = new Options(sqlBuild, SqlClause.INSERT, ", ", false, false, false, modelReader);
        Options valuesOptions = new Options(sqlBuild, SqlClause.VALUES, ", ", false, false, false, modelReader);
        for (Expression expression : insert.getSetClause().getExpressions()) {
            Equals equals = (Equals) expression;
            compileExpression(equals.getLeft(), insertOptions);
            compileExpression(equals.getRight(), valuesOptions);
        }
        buildCommonSqlOrder(insert, sqlBuild, false, dbmsSyntax, null, null, modelReader);
        if (dbmsSyntax.hasInsertReturningClause())
            sqlBuild.prepareAppend(new Options(sqlBuild, SqlClause.RETURNING, ", ", false, false, false, modelReader)).append(primaryKeySqlColumnName);
        return sqlBuild;
    }

    // Update compilation

    private static SqlBuild buildUpdate(Update update, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = createSqlOrderBuild(update, SqlClause.UPDATE, dbmsSyntax, null, modelReader);
        compileExpression(update.getSetClause(), new Options(sqlBuild, SqlClause.UPDATE, ", ", false, false, false, modelReader));
        return buildCommonSqlOrder(update, sqlBuild, false, dbmsSyntax, null, null, modelReader);
    }

    // Delete compilation

    private static SqlBuild buildDelete(Delete delete, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = createSqlOrderBuild(delete, SqlClause.DELETE, dbmsSyntax, null, modelReader);
        return buildCommonSqlOrder(delete, sqlBuild, false, dbmsSyntax, null, null, modelReader);
    }

    // Expression compilation

    public static void compileExpression(Expression expression, Options options) {
        Class<? extends Expression> expressionClass = expression.getClass();
        AbstractTermSqlCompiler termSqlCompiler = termCompilers.get(expressionClass);
        if (termSqlCompiler == null) {
            /* J2ME CLDC
            // trying to find the compiler of a super class (ex: DomainField compiler is actually a Term compiler)
            for (Class superClass = expressionClass.getSuperclass(); superClass != null; superClass = superClass.getSuperclass()) {
                termSqlCompiler = termCompilers.get(superClass);
                if (termSqlCompiler != null) {
                    termCompilers.put(expressionClass, termSqlCompiler);
                    break;
                }
            }
            if (termSqlCompiler == null) */
            throw new IllegalArgumentException("Sql not compilable: " + expression);
        }
        termSqlCompiler.compileExpressionToSql(expression, options);
    }

    /*** Helper methods ***/

    public static String toSqlString(String name) {
        if (name == null || name.length() < 2)
            return name;
        StringBuilder sb = new StringBuilder();
        boolean underscoreAllowed = false;
        int i0 = 0, i = 1;
        for (; i < name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i))) {
                if (underscoreAllowed)
                    sb.append('_');
                for (int j = i0; j < i; j++)
                    sb.append(Character.toLowerCase(name.charAt(j)));
                underscoreAllowed = i > i0 + 1; // no underscore between two consecutive uppercase
                i0 = i;
            }
        }
        if (underscoreAllowed)
            sb.append('_');
        for (int j = i0; j < i; j++)
            sb.append(Character.toLowerCase(name.charAt(j)));
        return sb.toString();
    }
}
