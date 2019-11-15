package webfx.framework.client.orm.entity.filter;

import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public final class EqlFilterBuilder {
    private final Object domainClassId;
    private String alias;
    private String fields;
    private String where;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;
    // The 'columns' field is for display purpose only, it is not included in the resulting sql query. So any persistent fields required for the columns evaluation should be loaded by including them in the 'fields' field.
    private String columns;

    public EqlFilterBuilder() {
        domainClassId = null;
    }

    public EqlFilterBuilder(Object jsonOrClass) {
        String s = jsonOrClass instanceof String ? (String) jsonOrClass : null;
        if ((s == null || s.indexOf('{') == -1) && !(jsonOrClass instanceof JsonObject))
            domainClassId = jsonOrClass;
        else {
            JsonObject json = s != null ? Json.parseObject(s) : (JsonObject) jsonOrClass;
            domainClassId = json.get("class");
            applyJson(json);
        }
    }

    public EqlFilterBuilder(JsonObject json) {
        domainClassId = json.get("class");
        applyJson(json);
    }

    public EqlFilterBuilder(EqlFilter eqlFilter) {
        domainClassId = eqlFilter.getDomainClassId();
        applyEqlFilter(eqlFilter);
    }

    private boolean isApplicable(EqlFilter sf) {
        return isApplicable(sf.getDomainClassId());
    }

    private boolean isApplicable(Object domainClassId) {
        return domainClassId == null || this.domainClassId != null && this.domainClassId.equals(domainClassId);
    }

    public EqlFilterBuilder applyEqlFilter(EqlFilter sf) {
        if (sf == null)
            return this;
        if (!isApplicable(sf))
            throw new IllegalArgumentException();
        setAlias(sf.getAlias());
        setFields(sf.getFields());
        setWhere(sf.getWhere());
        setGroupBy(sf.getGroupBy());
        setHaving(sf.getHaving());
        setOrderBy(sf.getOrderBy());
        setLimit(sf.getLimit());
        setColumns(sf.getColumns());
        return this;
    }

    public EqlFilterBuilder applyJson(JsonObject json) {
        if (json == null)
            return this;
        if (!isApplicable(json.getString("class")))
            throw new IllegalArgumentException();
        setAlias(json.getString("alias"));
        setFields(EqlFilter.getPossibleArrayAsString(json, "fields"));
        setWhere(json.getString("where"));
        setGroupBy(json.getString("groupBy"));
        setHaving(json.getString("having"));
        setOrderBy(json.getString("orderBy"));
        setLimit(json.getString("limit"));
        setColumns(EqlFilter.getPossibleArrayAsString(json, "columns"));
        return this;
    }

    public void merge(EqlFilter sf) {
        if (sf == null)
            return;
        if (!isApplicable(sf))
            throw new IllegalArgumentException("Trying to merge filters with different classes (" + domainClassId + " / " + sf.getDomainClassId() + ")");
        mergeAlias(sf.getAlias());
        mergeFields(sf.getFields());
        mergeWhere(sf.getWhere());
        mergeGroupBy(sf.getGroupBy());
        mergeHaving(sf.getHaving());
        mergeOrderBy(sf.getOrderBy());
        mergeLimit(sf.getLimit());
        mergeColumns(sf.getColumns());
    }

/*
    public static EqlFilter mergeEqlFilters(Object... args) {
        EqlFilter eqlFilter = (EqlFilter) args[0];
        if (args.length == 1)
            return eqlFilter;
        EqlFilterBuilder mergeBuilder = new EqlFilterBuilder(eqlFilter);
        for (int i = 1; i < args.length; i++)
            mergeBuilder.merge((EqlFilter) args[i]);
        return mergeBuilder.build();
    }
*/

    public String getColumns() {
        return columns;
    }

    /* Fluent API setters */

    public EqlFilterBuilder setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public EqlFilterBuilder setWhere(String where) {
        this.where = where;
        return this;
    }

    public EqlFilterBuilder setFields(String fields) {
        this.fields = fields;
        return this;
    }

    public EqlFilterBuilder setGroupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public EqlFilterBuilder setHaving(String having) {
        this.having = having;
        return this;
    }

    public EqlFilterBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public EqlFilterBuilder setLimit(String limit) {
        this.limit = limit;
        return this;
    }

    public EqlFilterBuilder setColumns(String columns) {
        this.columns = columns;
        return this;
    }

    /* Fluent API mergers */

    public EqlFilterBuilder mergeAlias(String alias) {
        if (alias != null)
            setAlias(alias);
        return this;
    }

    public EqlFilterBuilder mergeWhere(String where) {
        if (where != null && !where.isEmpty() && !"true".equals(where) && !"false".equals(this.where))
            setWhere(this.where == null || "true".equals(this.where) || "false".equals(where) ? where : "(" + this.where + ") and (" + where + ")");
        return this;
    }

    public EqlFilterBuilder mergeFields(String fields) {
        if (fields != null)
            setFields(mergeFields(this.fields, fields));
        return this;
    }

    public static String mergeFields(String fields1, String fields2) {
        return Strings.isEmpty(fields1) ? fields2 : Strings.isEmpty(fields2) ? fields1 : fields1 + ',' + fields2;
    }

    public EqlFilterBuilder mergeGroupBy(String groupBy) {
        if (groupBy != null)
            setGroupBy(groupBy);
        return this;
    }

    public EqlFilterBuilder mergeHaving(String having) {
        if (having != null)
            setHaving(having);
        return this;
    }

    public EqlFilterBuilder mergeOrderBy(String orderBy) {
        if (orderBy != null)
            setOrderBy(orderBy);
        return this;
    }

    public EqlFilterBuilder mergeLimit(String limit) {
        if (limit != null)
            setLimit(limit);
        return this;
    }

    public EqlFilterBuilder mergeColumns(String columns) {
        if (columns != null)
            setColumns(columns);
        return this;
    }

    public static String mergeColumns(String columns1, String columns2) {
        return Strings.isEmpty(columns1) ? columns2 : Strings.isEmpty(columns2) ? columns1 : Strings.removeSuffix(columns1, "]") + ',' + Strings.removePrefix(columns2, "[");
    }

    public EqlFilter build() {
        return new EqlFilter(domainClassId, alias, fields, where, groupBy, having, orderBy, limit, columns);
    }
}
