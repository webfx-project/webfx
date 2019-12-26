package webfx.framework.shared.orm.domainmodel;

import webfx.framework.shared.orm.domainmodel.lciimpl.CompilerDomainModelReaderImpl;
import webfx.framework.shared.orm.dql.sqlcompiler.ExpressionSqlCompiler;
import webfx.framework.shared.orm.dql.sqlcompiler.lci.CompilerDomainModelReader;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms.DbmsSqlSyntax;
import webfx.framework.shared.orm.expression.terms.DqlStatement;
import webfx.framework.shared.orm.expression.terms.Select;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class DataSourceModel implements HasDomainModel {

    private final Object dataSourceId;
    private final DbmsSqlSyntax dbmsSqlSyntax;
    private final DomainModel domainModel;
    private CompilerDomainModelReader compilerDomainModelReader;
    private Map<String, SqlCompiled> sqlCompiledCache = new /*Weak*/HashMap<>();

    public DataSourceModel(Object dataSourceId, DbmsSqlSyntax dbmsSqlSyntax, DomainModel domainModel) {
        this.dataSourceId = dataSourceId;
        this.dbmsSqlSyntax = dbmsSqlSyntax;
        this.domainModel = domainModel;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public DbmsSqlSyntax getDbmsSqlSyntax() {
        return dbmsSqlSyntax;
    }

    @Override
    public DomainModel getDomainModel() {
        return domainModel;
    }

    public CompilerDomainModelReader getCompilerDomainModelReader() {
        if (compilerDomainModelReader == null)
            compilerDomainModelReader = new CompilerDomainModelReaderImpl(getDomainModel());
        return compilerDomainModelReader;
    }

    public SqlCompiled parseAndCompileSelect(String stringSelect) {
        SqlCompiled sqlCompiled = sqlCompiledCache.get(stringSelect);
        //if (sqlCompiled != null) Logger.log("Reusing cached sql compiled! :-)");
        if (sqlCompiled == null)
            sqlCompiledCache.put(stringSelect, sqlCompiled = compileSelect(parseSelect(stringSelect)));
        return sqlCompiled;
    }

    public <T> Select<T> parseSelect(String definition) {
        return getDomainModel().parseSelect(definition);
    }


    public SqlCompiled compileSelect(Select<?> select) {
        return ExpressionSqlCompiler.compileSelect(select, getDbmsSqlSyntax(), true, true, getCompilerDomainModelReader());
    }

    public String translateQuery(String queryLanguage, String query) {
        if ("DQL".equalsIgnoreCase(queryLanguage))
            return parseAndCompileSelect(query).getSql();
        return query;
    }

    public SqlCompiled parseAndCompileStatement(String statement) {
        SqlCompiled sqlCompiled = sqlCompiledCache.get(statement);
        //if (sqlCompiled != null) Logger.log("Reusing cached sql compiled! :-)");
        if (sqlCompiled == null)
            sqlCompiledCache.put(statement, sqlCompiled = compileStatement(parseStatement(statement)));
        return sqlCompiled;
    }

    public <T> DqlStatement<T> parseStatement(String definition) {
        return getDomainModel().parseStatement(definition);
    }

    public SqlCompiled compileStatement(DqlStatement<?> statement) {
        return ExpressionSqlCompiler.compileStatement(statement, getDbmsSqlSyntax(), getCompilerDomainModelReader());
    }

    public String translateStatementIfDql(String statementLang, String statementString) {
        if ("DQL".equalsIgnoreCase(statementLang))
            return parseAndCompileStatement(statementString).getSql();
        return statementString;
    }
}
