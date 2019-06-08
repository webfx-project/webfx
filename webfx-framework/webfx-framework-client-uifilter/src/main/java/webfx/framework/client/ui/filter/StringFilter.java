package webfx.framework.client.ui.filter;

import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public final class StringFilter {
    private final Object domainClassId;
    private final String alias;
    private final String fields;
    private final String where;
    private final String groupBy;
    private final String having;
    private final String orderBy;
    private final String limit;
    // The 'columns' field is for display purpose only, it is not included in the resulting sql query. So any persistent fields required for the columns evaluation should be loaded by including them in the 'fields' field.
    private final String columns;

    public StringFilter(Object domainClassId, String alias, String fields, String where, String groupBy, String having, String orderBy, String limit, String columns) {
        this.domainClassId = domainClassId;
        this.alias = alias;
        this.fields = fields;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
        this.columns = columns;
    }

    public StringFilter(JsonObject json) {
        domainClassId = json.get("class");
        alias = json.getString("alias");
        fields = getPossibleArrayAsString(json, "fields");
        where = json.getString("where");
        groupBy = json.getString("groupBy");
        having = json.getString("having");
        orderBy = json.getString("orderBy");
        limit = json.getString("limit");
        columns = getPossibleArrayAsString(json, "columns");
    }

    static String getPossibleArrayAsString(JsonObject json, String key) {
        Object nativeElement = json.getNativeElement(key);
        return nativeElement == null ? null : nativeElement instanceof String ? (String) nativeElement : Json.toJsonString(nativeElement);
    }

    public StringFilter(String json) {
        this(Json.parseObject(json));
    }

    public Object getDomainClassId() {
        return domainClassId;
    }

    public String getAlias() {
        return alias;
    }

    public String getFields() {
        return fields;
    }

    public String getWhere() {
        return where;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public String getHaving() {
        return having;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getLimit() {
        return limit;
    }

    public String getColumns() {
        return columns;
    }

    public String toStringSelect() {
        return "select " +
                (fields == null ? "" : fields + " from ") +
                domainClassId +
                (alias == null ? "" : ' ' + alias) +
                (where == null ? "" : " where " + where) +
                (groupBy == null ? "" : " group by " + groupBy) +
                (having == null ? "" : " having " + having) +
                (orderBy == null ? "" : " order by " + orderBy) +
                (limit == null ? "" : " limit " + limit);
    }

    public String toStringJson() {
        return "{class: " + (domainClassId == null ? "null" : "`" + domainClassId + '`') +
                (fields == null ? "" : ", fields: `" + fields + '`') +
                (columns == null ? "" : ", columns: `" + columns + '`') +
                (alias == null ? "" : ", alias: `" + alias + '`') +
                (where == null ? "" : ", where: `" + where + '`') +
                (groupBy == null ? "" : ", groupBy: `" + groupBy + '`') +
                (having == null ? "" : ", having: `" + having + '`') +
                (orderBy == null ? "" : ", orderBy: `" + orderBy + '`') +
                (limit == null ? "" : ", limit: `" + limit + '`') +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringFilter that = (StringFilter) o;

        if (domainClassId != null ? !domainClassId.equals(that.domainClassId) : that.domainClassId != null)
            return false;
        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (fields != null ? !fields.equals(that.fields) : that.fields != null) return false;
        if (where != null ? !where.equals(that.where) : that.where != null) return false;
        if (groupBy != null ? !groupBy.equals(that.groupBy) : that.groupBy != null) return false;
        if (having != null ? !having.equals(that.having) : that.having != null) return false;
        if (orderBy != null ? !orderBy.equals(that.orderBy) : that.orderBy != null) return false;
        if (limit != null ? !limit.equals(that.limit) : that.limit != null) return false;
        return columns != null ? columns.equals(that.columns) : that.columns == null;

    }

    @Override
    public int hashCode() {
        int result = domainClassId != null ? domainClassId.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        result = 31 * result + (where != null ? where.hashCode() : 0);
        result = 31 * result + (groupBy != null ? groupBy.hashCode() : 0);
        result = 31 * result + (having != null ? having.hashCode() : 0);
        result = 31 * result + (orderBy != null ? orderBy.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        return result;
    }
}
