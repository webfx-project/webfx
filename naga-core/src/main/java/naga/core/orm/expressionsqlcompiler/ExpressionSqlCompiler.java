package naga.core.orm.expressionsqlcompiler;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Select;
import naga.core.orm.expressionsqlcompiler.lci.CompilerDomainModelReader;
import naga.core.orm.expressionsqlcompiler.sql.DbmsSqlSyntaxOptions;
import naga.core.orm.expressionsqlcompiler.sql.SqlBuild;
import naga.core.orm.expressionsqlcompiler.sql.SqlClause;
import naga.core.orm.expressionsqlcompiler.sql.SqlCompiled;
import naga.core.orm.expressionsqlcompiler.term.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ExpressionSqlCompiler {

    private static Map<Class<? extends Expression>, AbstractTermSqlCompiler<?>> termCompilers = new HashMap<>();

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
                new SymbolSqlCompiler()             // Symbol
        );
    }

    private static void registerTermCompilers(AbstractTermSqlCompiler<?>... termSqlCompilers) {
        for (AbstractTermSqlCompiler<?> expressionSqlCompiler : termSqlCompilers)
            for (Class<? extends Expression> expressionClass : expressionSqlCompiler.getSupportedTermClasses())
                termCompilers.put(expressionClass, expressionSqlCompiler);
    }

    /**
     * Select compilation
     */

    // Public entry points

    public static SqlCompiled compileSelect(Select select, Object[] parameterValues, DbmsSqlSyntaxOptions dbmsSyntax, boolean generateQueryMapping, boolean readForeignFields, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = compileSelect(select, parameterValues, dbmsSyntax, generateQueryMapping, readForeignFields, null, null, modelReader);
        return sqlBuild.toSqlCompiled();
    }

    // Private entry points

    public static SqlBuild compileSelect(Select select, Options parentOptions) {
        Options o = parentOptions;
        return compileSelect(select, null /*o.parameterValues - can it disappear?*/, o.build.getDbmsSyntax(), o.generateQueryMapping, o.readForeignFields, o.build, o.clause, o.modelReader);
    }

    public static SqlBuild compileSelect(Select select, Object[] parameterValues, DbmsSqlSyntaxOptions dbmsSyntax, boolean generateQueryMapping, boolean readForeignFields, SqlBuild parent, SqlClause parentClause, CompilerDomainModelReader modelReader) {
        SqlBuild sqlBuild = new SqlBuild(parent, select.getDomainClass(), select.getDomainClassAlias(), SqlClause.SELECT, dbmsSyntax, modelReader);
        sqlBuild.setDistinct(select.isDistinct());
        boolean grouped = select.getGroupBy() != null;
        if (select.isIncludeIdColumn())
            sqlBuild.addColumnInClause(sqlBuild.getTableAlias(), modelReader.getDomainClassPrimaryKeySqlColumnName(select.getDomainClass()), null, null, SqlClause.SELECT, "", grouped, false, true);
        if (select.getFields() != null)
            compileExpression(select.getFields(), new Options(sqlBuild, SqlClause.SELECT, ", ", parameterValues, grouped, generateQueryMapping, readForeignFields, modelReader));
        if (select.getWhere() != null)
            compileExpression(select.getWhere(), new Options(sqlBuild, SqlClause.WHERE, null, parameterValues, grouped, false, false, modelReader));
        if (select.getGroupBy() != null)
            compileExpression(select.getGroupBy(), new Options(sqlBuild, SqlClause.GROUP_BY, ", ", parameterValues, grouped, false, false, modelReader));
        if (select.getHaving() != null)
            compileExpression(select.getHaving(), new Options(sqlBuild, SqlClause.HAVING, ", ", parameterValues, grouped, false, false, modelReader));
        if (select.getOrderBy() != null)
            compileExpression(select.getOrderBy(), new Options(sqlBuild, SqlClause.ORDER_BY, ", ", parameterValues, grouped, false, false, modelReader));
        if (select.getLimit() != null && dbmsSyntax != DbmsSqlSyntaxOptions.HSQL_SYNTAX) // temporary fix
            compileExpression(select.getLimit(), new Options(sqlBuild, SqlClause.LIMIT, null, parameterValues, grouped, false, false, modelReader));
        if (parent != null)
            parent.prepareAppend(parentClause, null).append(sqlBuild.toSql()); // is it the right way?
        select.setCacheable(sqlBuild.isCacheable());
        return sqlBuild;
    }

    public static void compileExpression(Expression expression, Options options) {
        Class<? extends Expression> expressionClass = expression.getClass();
        AbstractTermSqlCompiler termSqlCompiler = termCompilers.get(expressionClass);
        if (termSqlCompiler == null) {
            // trying to find the compiler of a super class (ex: DomainField compiler is actually a Term compiler)
            for (Class superClass = expressionClass.getSuperclass(); superClass != null; superClass = superClass.getSuperclass()) {
                termSqlCompiler = termCompilers.get(superClass);
                if (termSqlCompiler != null) {
                    termCompilers.put(expressionClass, termSqlCompiler);
                    break;
                }
            }
            if (termSqlCompiler == null)
                throw new IllegalArgumentException("Sql not compilable: " + expression);
        }
        termSqlCompiler.compileExpressionToSql(expression, options);
    }

    /*** Helper methods ***/

    public static String toSqlString(String name) {
        if (name == null || name.length() == 1)
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
