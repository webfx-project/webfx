package webfx.framework.shared.orm.dql;

import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public final class DqlStatementBuilder {

    private final Object domainClassId;
    private String alias;
    private String fields;
    private DqlClause where;
    private DqlClause groupBy;
    private DqlClause having;
    private DqlClause orderBy;
    private DqlClause limit;
    // The 'columns' field is for display purpose only, it is not included in the resulting sql query. So any persistent fields required for the columns evaluation should be loaded by including them in the 'fields' field.
    private String columns;

    public DqlStatementBuilder() {
        domainClassId = null;
    }

    public DqlStatementBuilder(Object jsonOrClass) {
        String s = jsonOrClass instanceof String ? (String) jsonOrClass : null;
        if ((s == null || s.indexOf('{') == -1) && !(jsonOrClass instanceof JsonObject))
            domainClassId = jsonOrClass;
        else {
            JsonObject json = s != null ? Json.parseObject(s) : (JsonObject) jsonOrClass;
            domainClassId = json.get("class");
            applyJson(json);
        }
    }

    public DqlStatementBuilder(JsonObject json) {
        domainClassId = json.get("class");
        applyJson(json);
    }

    public DqlStatementBuilder(DqlStatement dqlStatement) {
        domainClassId = dqlStatement.getDomainClassId();
        applyFilter(dqlStatement);
    }

    public DqlStatement build() {
        return new DqlStatement(domainClassId, alias, fields, where, groupBy, having, orderBy, limit, columns);
    }

    public String getColumns() {
        return columns;
    }

    private boolean isApplicable(DqlStatement dqlStatement) {
        return isApplicable(dqlStatement.getDomainClassId());
    }

    private boolean isApplicable(Object domainClassId) {
        return domainClassId == null || this.domainClassId != null && this.domainClassId.equals(domainClassId);
    }

    public DqlStatementBuilder applyFilter(DqlStatement f) {
        if (f == null)
            return this;
        if (!isApplicable(f))
            throw new IllegalArgumentException();
        setAlias(f.getAlias());
        setFields(f.getFields());
        setWhere(f.getWhere());
        setGroupBy(f.getGroupBy());
        setHaving(f.getHaving());
        setOrderBy(f.getOrderBy());
        setLimit(f.getLimit());
        setColumns(f.getColumns());
        return this;
    }

    public DqlStatementBuilder applyJson(JsonObject json) {
        if (json == null)
            return this;
        if (!isApplicable(json.getString("class")))
            throw new IllegalArgumentException();
        setAlias(json.getString("alias"));
        setFields(DqlStatement.getPossibleArrayAsString(json, "fields"));
        setWhere(DqlClause.create(json.getString("where")));
        setGroupBy(DqlClause.create(json.getString("groupBy")));
        setHaving(DqlClause.create(json.getString("having")));
        setOrderBy(DqlClause.create(json.getString("orderBy")));
        setLimit(DqlClause.create(json.getString("limit")));
        setColumns(DqlStatement.getPossibleArrayAsString(json, "columns"));
        return this;
    }

    /* Fluent API setters */

    public DqlStatementBuilder setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public DqlStatementBuilder setWhere(DqlClause where) {
        this.where = where;
        return this;
    }

    public DqlStatementBuilder setFields(String fields) {
        this.fields = fields;
        return this;
    }

    public DqlStatementBuilder setGroupBy(DqlClause groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public DqlStatementBuilder setHaving(DqlClause having) {
        this.having = having;
        return this;
    }

    public DqlStatementBuilder setOrderBy(DqlClause orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public DqlStatementBuilder setLimit(DqlClause limit) {
        this.limit = limit;
        return this;
    }

    public DqlStatementBuilder setColumns(String columns) {
        this.columns = columns;
        return this;
    }


    public void merge(DqlStatement f) {
        if (f == null)
            return;
        if (!isApplicable(f))
            throw new IllegalArgumentException("Trying to merge filters with different classes (" + domainClassId + " / " + f.getDomainClassId() + ")");
        mergeAlias(f.getAlias());
        mergeFields(f.getFields());
        mergeWhere(f.getWhere());
        mergeGroupBy(f.getGroupBy());
        mergeHaving(f.getHaving());
        mergeOrderBy(f.getOrderBy());
        mergeLimit(f.getLimit());
        mergeColumns(f.getColumns());
    }

    /* Fluent API mergers */

    public DqlStatementBuilder mergeAlias(String alias) {
        if (alias != null)
            setAlias(alias);
        return this;
    }

    public DqlStatementBuilder mergeWhere(DqlClause where) {
        if (where != null && !DqlClause.isClauseFalse(this.where) && !DqlClause.isClauseTrue(where)) {
            if (this.where == null || DqlClause.isClauseFalse(where) || DqlClause.isClauseTrue(this.where))
                this.where = where;
            else
                this.where = DqlClause.create("(" + this.where.getDql() + ") and (" + where.getDql() + ")", DqlClause.concatClauseParameterValues(this.where, where));
        }
        return this;
    }

    public DqlStatementBuilder mergeFields(String fields) {
        if (fields != null)
            setFields(mergeFields(this.fields, fields));
        return this;
    }

    public static String mergeFields(String fields1, String fields2) {
        return Strings.isEmpty(fields1) ? fields2 : Strings.isEmpty(fields2) ? fields1 : fields1 + ',' + fields2;
    }

    public DqlStatementBuilder mergeGroupBy(DqlClause groupBy) {
        if (groupBy != null)
            setGroupBy(groupBy);
        return this;
    }

    public DqlStatementBuilder mergeHaving(DqlClause having) {
        if (having != null)
            setHaving(having);
        return this;
    }

    public DqlStatementBuilder mergeOrderBy(DqlClause orderBy) {
        if (orderBy != null)
            setOrderBy(orderBy);
        return this;
    }

    public DqlStatementBuilder mergeLimit(DqlClause limit) {
        if (limit != null)
            setLimit(limit);
        return this;
    }

    public DqlStatementBuilder mergeColumns(String columns) {
        if (columns != null)
            setColumns(columns);
        return this;
    }

    public static String mergeColumns(String columns1, String columns2) {
        return Strings.isEmpty(columns1) ? columns2 : Strings.isEmpty(columns2) ? columns1 : Strings.removeSuffix(columns1, "]") + ',' + Strings.removePrefix(columns2, "[");
    }

}
