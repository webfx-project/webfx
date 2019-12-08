package webfx.framework.shared.orm.domainmodel;

import webfx.framework.shared.orm.domainmodel.lciimpl.CompilerDomainModelReaderImpl;
import webfx.framework.shared.orm.dql.sqlcompiler.ExpressionSqlCompiler;
import webfx.framework.shared.orm.dql.sqlcompiler.lci.CompilerDomainModelReader;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms.DbmsSqlSyntax;
import webfx.framework.shared.orm.expression.terms.Select;
import webfx.framework.shared.services.domainmodel.DomainModelService;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class DataSourceModel implements HasDomainModel {

    private final Object dataSourceId;
    private final DbmsSqlSyntax dbmsSqlSyntax;
    private CompilerDomainModelReader compilerDomainModelReader;
    private Map<String, SqlCompiled> sqlCompiledCache = new /*Weak*/HashMap<>();

    public DataSourceModel(Object dataSourceId, DbmsSqlSyntax dbmsSqlSyntax) {
        this.dataSourceId = dataSourceId;
        this.dbmsSqlSyntax = dbmsSqlSyntax;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public DbmsSqlSyntax getDbmsSqlSyntax() {
        return dbmsSqlSyntax;
    }

    public Future<DomainModel> getOrLoadDomainModel() {
        return DomainModelService.loadDomainModel(dataSourceId);
    }

    @Override
    public DomainModel getDomainModel() {
        return getOrLoadDomainModel().result();
    }

    public CompilerDomainModelReader getCompilerDomainModelReader() {
        if (compilerDomainModelReader == null)
            compilerDomainModelReader = new CompilerDomainModelReaderImpl(getDomainModel());
        return compilerDomainModelReader;
    }

    public SqlCompiled parseAndCompileSelect(String stringSelect) {
        SqlCompiled sqlCompiled = sqlCompiledCache.get(stringSelect);
        if (sqlCompiled != null)
            Logger.log("Reusing cached sql compiled! :-)");
        else
            sqlCompiledCache.put(stringSelect, sqlCompiled = compileSelect(getDomainModel().parseSelect(stringSelect)));
        return sqlCompiled;
    }

    public SqlCompiled compileSelect(Select<?> select) {
        return ExpressionSqlCompiler.compileSelect(select, getDbmsSqlSyntax(), true, true, getCompilerDomainModelReader());
    }

    public String translateQuery(String queryLang, String query) {
        if ("DQL".equalsIgnoreCase(queryLang))
            return parseAndCompileSelect(query).getSql();
        return query;
    }
}
