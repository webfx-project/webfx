package webfx.framework.shared.orm.dql;

import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class DqlStatement {

    public static final DqlStatement EMPTY_STATEMENT = where("false");

    private final Object domainClassId;
    private final String alias;
    private final String fields;
    private final DqlClause where;
    private final DqlClause groupBy;
    private final DqlClause having;
    private final DqlClause orderBy;
    private final DqlClause limit;
    // The 'columns' field is for display purpose only, it is not included in the query. So any persistent fields required for the columns evaluation should be loaded by including them in the 'fields' field.
    private final String columns;

    private Object[] selectParameterValues;

    public static DqlStatement parse(CharSequence dqlStatementString) {
        return dqlStatementString == null ? null : new DqlStatement(dqlStatementString.toString());
    }

    public static DqlStatement fields(CharSequence fields) {
        return new DqlStatement(null, null, fields.toString(), null, null, null, null, null, null);
    }

    public static DqlStatement where(CharSequence where, Object... parameterValues) {
        return new DqlStatement(null, null, null, DqlClause.create(where, parameterValues), null, null, null, null, null);
    }

    public static DqlStatement limit(CharSequence limit, Object... parameterValues) {
        return new DqlStatement(null, null, null, null, null, null, null, DqlClause.create(limit, parameterValues), null);
    }

    public DqlStatement(Object domainClassId, String alias, String fields, DqlClause where, DqlClause groupBy, DqlClause having, DqlClause orderBy, DqlClause limit, String columns) {
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

    public DqlStatement(JsonObject json) {
        domainClassId = json.get("class");
        alias = json.getString("alias");
        fields = getPossibleArrayAsString(json, "fields");
        where = DqlClause.create(json.getString("where"));
        groupBy = DqlClause.create(json.getString("groupBy"));
        having = DqlClause.create(json.getString("having"));
        orderBy = DqlClause.create(json.getString("orderBy"));
        limit = DqlClause.create(json.getString("limit"));
        columns = getPossibleArrayAsString(json, "columns");
    }

    public DqlStatement(String dqlStatementString) {
        this(Json.parseObject(dqlStatementString));
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

    public DqlClause getWhere() {
        return where;
    }

    public DqlClause getGroupBy() {
        return groupBy;
    }

    public DqlClause getHaving() {
        return having;
    }

    public DqlClause getOrderBy() {
        return orderBy;
    }

    public DqlClause getLimit() {
        return limit;
    }

    public String getColumns() {
        return columns;
    }

    public Object[] getSelectParameterValues() {
        if (selectParameterValues == null)
            selectParameterValues = DqlClause.concatClauseParameterValues(where, groupBy, having, orderBy, limit);
        return selectParameterValues;
    }

    public boolean isInherentlyEmpty() {
        return DqlClause.isClauseFalse(where) || DqlClause.isClause0(limit);
    }

    public String toDqlSelect() {
        return "select " +
                (fields == null ? "" : fields + " from ") +
                domainClassId +
                (alias == null ? "" : ' ' + alias) +
                (where == null ? "" : " where " + where.getDql()) +
                (groupBy == null ? "" : " group by " + groupBy.getDql()) +
                (having == null ? "" : " having " + having.getDql()) +
                (orderBy == null ? "" : " order by " + orderBy.getDql()) +
                (limit == null ? "" : " limit " + limit.getDql());
    }

    public String toStringJson() {
        return "{class: " + (domainClassId == null ? "null" : "`" + domainClassId + '`') +
                (fields == null ? "" : ", fields: `" + fields + '`') +
                (columns == null ? "" : ", columns: `" + columns + '`') +
                (alias == null ? "" : ", alias: `" + alias + '`') +
                (where == null ? "" : ", where: `" + where.getDql() + '`') +
                (groupBy == null ? "" : ", groupBy: `" + groupBy.getDql() + '`') +
                (having == null ? "" : ", having: `" + having.getDql() + '`') +
                (orderBy == null ? "" : ", orderBy: `" + orderBy.getDql() + '`') +
                (limit == null ? "" : ", limit: `" + limit.getDql() + '`') +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DqlStatement dqlStatement = (DqlStatement) o;

        if (domainClassId != null ? !domainClassId.equals(dqlStatement.domainClassId) : dqlStatement.domainClassId != null)
            return false;
        if (alias != null ? !alias.equals(dqlStatement.alias) : dqlStatement.alias != null) return false;
        if (fields != null ? !fields.equals(dqlStatement.fields) : dqlStatement.fields != null) return false;
        if (where != null ? !where.equals(dqlStatement.where) : dqlStatement.where != null) return false;
        if (groupBy != null ? !groupBy.equals(dqlStatement.groupBy) : dqlStatement.groupBy != null) return false;
        if (having != null ? !having.equals(dqlStatement.having) : dqlStatement.having != null) return false;
        if (orderBy != null ? !orderBy.equals(dqlStatement.orderBy) : dqlStatement.orderBy != null) return false;
        if (limit != null ? !limit.equals(dqlStatement.limit) : dqlStatement.limit != null) return false;
        if (columns != null ? !columns.equals(dqlStatement.columns) : dqlStatement.columns != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(selectParameterValues, dqlStatement.selectParameterValues);
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
        result = 31 * result + Arrays.hashCode(selectParameterValues);
        return result;
    }

    static String getPossibleArrayAsString(JsonObject json, String key) {
        Object nativeElement = json.getNativeElement(key);
        return nativeElement == null ? null : nativeElement instanceof String ? (String) nativeElement : Json.toJsonString(nativeElement);
    }
}
