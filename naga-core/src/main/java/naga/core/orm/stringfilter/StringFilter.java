package naga.core.orm.stringfilter;

import naga.core.json.JsonObject;
import naga.core.json.Json;

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

    public StringFilter(Object domainClassId, String alias, String fields, String where, String groupBy, String having, String orderBy, String limit) {
        this.domainClassId = domainClassId;
        this.alias = alias;
        this.fields = fields;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    public StringFilter(JsonObject json) {
        domainClassId = json.get("class");
        alias = json.getString("alias");
        fields = json.getString("fields");
        where = json.getString("where");
        groupBy = json.getString("groupBy");
        having = json.getString("having");
        orderBy = json.getString("orderBy");
        limit = json.getString("limit");
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
        return limit != null ? limit.equals(that.limit) : that.limit == null;

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
        return result;
    }
}
