package naga.framework.ui.filter;

import naga.platform.json.spi.JsonObject;
import naga.platform.json.Json;
import naga.commons.util.Strings;

/**
 * @author Bruno Salmon
 */
public class StringFilterBuilder {
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

    public StringFilterBuilder() {
        domainClassId = null;
    }

    public StringFilterBuilder(Object jsonOrClass) {
        String s = jsonOrClass instanceof String ? (String) jsonOrClass : null;
        if (s == null || s.indexOf('{') == -1)
            this.domainClassId = jsonOrClass;
        else {
            JsonObject json = Json.parseObject(s);
            this.domainClassId = json.get("class");
            applyJson(json);
        }
    }

    public StringFilterBuilder(JsonObject json) {
        this.domainClassId = json.get("class");
        applyJson(json);
    }

    public StringFilterBuilder(StringFilter sf) {
        domainClassId = sf.getDomainClassId();
        applyStringFilter(sf);
    }

    private boolean isApplicable(StringFilter sf) {
        return isApplicable(sf.getDomainClassId());
    }

    private boolean isApplicable(Object domainClassId) {
        boolean applicable = domainClassId == null || this.domainClassId != null && this.domainClassId.equals(domainClassId);
        if (!applicable)
            System.out.println("Not applicable!!!");
        return applicable;
    }

    public StringFilterBuilder applyStringFilter(StringFilter sf) {
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

    public StringFilterBuilder applyJson(JsonObject json) {
        if (json == null)
            return this;
        if (!isApplicable(json.getString("class")))
            throw new IllegalArgumentException();
        setAlias(json.getString("alias"));
        setFields(json.getString("fields"));
        setWhere(json.getString("where"));
        setGroupBy(json.getString("groupBy"));
        setHaving(json.getString("having"));
        setOrderBy(json.getString("orderBy"));
        setLimit(json.getString("limit"));
        setColumns(json.getString("columns"));
        return this;
    }

    public void merge(StringFilter sf) {
        if (sf == null)
            return;
        if (!isApplicable(sf))
            throw new IllegalArgumentException("Trying to merge filters of different classes (" + domainClassId + " / " + sf.getDomainClassId() + ")");
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
    public static StringFilter mergeStringFilters(Object... args) {
        StringFilter stringFilter = (StringFilter) args[0];
        if (args.length == 1)
            return stringFilter;
        StringFilterBuilder mergeBuilder = new StringFilterBuilder(stringFilter);
        for (int i = 1; i < args.length; i++)
            mergeBuilder.merge((StringFilter) args[i]);
        return mergeBuilder.build();
    }
*/

    public String getColumns() {
        return columns;
    }

    /* Fluent API setters */

    public StringFilterBuilder setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public StringFilterBuilder setWhere(String where) {
        this.where = where;
        return this;
    }

    public StringFilterBuilder setFields(String fields) {
        this.fields = fields;
        return this;
    }

    public StringFilterBuilder setGroupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public StringFilterBuilder setHaving(String having) {
        this.having = having;
        return this;
    }

    public StringFilterBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public StringFilterBuilder setLimit(String limit) {
        this.limit = limit;
        return this;
    }

    public StringFilterBuilder setColumns(String columns) {
        this.columns = columns;
        return this;
    }

    /* Fluent API mergers */

    public StringFilterBuilder mergeAlias(String alias) {
        if (alias != null)
            setAlias(alias);
        return this;
    }

    public StringFilterBuilder mergeWhere(String where) {
        if (where != null && !"true".equals(where) && !"false".equals(this.where))
            setWhere(this.where == null || "true".equals(this.where) || "false".equals(where) ? where : "(" + this.where + ") and (" + where + ")");
        return this;
    }

    public StringFilterBuilder mergeFields(String fields) {
        if (fields != null)
            setFields(mergeFields(this.fields, fields));
        return this;
    }

    public static String mergeFields(String fields1, String fields2) {
        return Strings.isEmpty(fields1) ? fields2 : Strings.isEmpty(fields2) ? fields1 : fields1 + ',' + fields2;
    }

    public StringFilterBuilder mergeGroupBy(String groupBy) {
        if (groupBy != null)
            setGroupBy(groupBy);
        return this;
    }

    public StringFilterBuilder mergeHaving(String having) {
        if (having != null)
            setHaving(having);
        return this;
    }

    public StringFilterBuilder mergeOrderBy(String orderBy) {
        if (orderBy != null)
            setOrderBy(orderBy);
        return this;
    }

    public StringFilterBuilder mergeLimit(String limit) {
        if (limit != null)
            setLimit(limit);
        return this;
    }

    public StringFilterBuilder mergeColumns(String columns) {
        if (columns != null)
            setColumns(mergeFields(this.columns, columns));
        return this;
    }

    public static String mergeColumns(String columns1, String columns2) {
        return Strings.isEmpty(columns1) ? columns2 : Strings.isEmpty(columns2) ? columns1 : Strings.removeSuffix(columns1, "]") + ',' + Strings.removePrefix(columns2, "[");
    }

    public StringFilter build() {
        return new StringFilter(domainClassId, alias, fields, where, groupBy, having, orderBy, limit, columns);
    }
}
