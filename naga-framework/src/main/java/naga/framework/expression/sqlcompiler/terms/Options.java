package naga.framework.expression.sqlcompiler.terms;

import naga.framework.expression.lci.CompilerDomainModelReader;
import naga.framework.expression.sqlcompiler.sql.SqlBuild;
import naga.framework.expression.sqlcompiler.sql.SqlClause;
import naga.util.Objects;

/**
 * @author Bruno Salmon
 */
public class Options {
    public final SqlBuild build;
    public final SqlClause clause;
    public String separator;
    public Object[] parameterValues;
    public boolean grouped;
    public boolean generateQueryMapping;
    public boolean readForeignFields;
    public CompilerDomainModelReader modelReader;

    public Options(SqlBuild build, SqlClause clause, String separator, Object[] parameterValues, boolean grouped, boolean generateQueryMapping, boolean readForeignFields, CompilerDomainModelReader modelReader) {
        this.separator = separator;
        this.build = build;
        this.clause = clause;
        this.parameterValues = parameterValues;
        this.grouped = grouped;
        this.generateQueryMapping = generateQueryMapping;
        this.readForeignFields = readForeignFields;
        this.modelReader = modelReader;
    }

    public Options changeSeparator(String separator) {
        if (Objects.areEquals(this.separator, separator))
            return this;
        return new Options(build, clause, separator, parameterValues, grouped, generateQueryMapping, readForeignFields, modelReader);
    }

    public Options changeReadForeignFields(boolean readForeignFields) {
        if (this.readForeignFields == readForeignFields)
            return this;
        return new Options(build, clause, separator, parameterValues, grouped, generateQueryMapping, readForeignFields, modelReader);
    }

    public Options changeGenerateQueryMapping(boolean generateQueryMapping) {
        if (this.generateQueryMapping == generateQueryMapping)
            return this;
        return new Options(build, clause, separator, parameterValues, grouped, generateQueryMapping, readForeignFields, modelReader);
    }

    public Options changeSeparatorGenerateQueryMapping(String separator, boolean generateQueryMapping) {
        if (Objects.areEquals(this.separator, separator) && this.generateQueryMapping == generateQueryMapping)
            return this;
        return new Options(build, clause, separator, parameterValues, grouped, generateQueryMapping, readForeignFields, modelReader);
    }

    public Options changeSeparatorGroupedGenerateQueryMapping(String separator, boolean grouped, boolean generateQueryMapping) {
        if (Objects.areEquals(this.separator, separator) && this.grouped == grouped && this.generateQueryMapping == generateQueryMapping)
            return this;
        return new Options(build, clause, separator, parameterValues, grouped, generateQueryMapping, readForeignFields, modelReader);
    }
}
