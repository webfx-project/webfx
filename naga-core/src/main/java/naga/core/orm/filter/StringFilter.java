package naga.core.orm.filter;

/**
 * @author Bruno Salmon
 */
public final class StringFilter {
    private final Object domainClassId;
    private final String alias;
    private final String displayFields;
    private final String logicFields;
    private final String condition;
    private final String groupBy;
    private final String having;
    private final String orderBy;
    private final String limit;

    public StringFilter(Object domainClassId, String alias, String displayFields, String logicFields, String condition, String groupBy, String having, String orderBy, String limit) {
        this.domainClassId = domainClassId;
        this.alias = alias;
        this.displayFields = displayFields;
        this.logicFields = logicFields;
        this.condition = condition;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    public Object getDomainClassId() {
        return domainClassId;
    }

    public String getAlias() {
        return alias;
    }

    public String getDisplayFields() {
        return displayFields;
    }

    public String getLogicFields() {
        return logicFields;
    }

    public String getAllFields() {
        return StringFilterBuilder.mergeFields(displayFields, logicFields);
    }

    public String getCondition() {
        return condition;
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
        return toStringSelect(getAllFields());
    }

    public String toStringSelect(String allFields) {
        return "select " +
                (allFields == null ? "" : allFields + " from ") +
                domainClassId +
                (alias == null ? "" : ' ' + alias) +
                (condition == null ? "" : " where " + condition) +
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

        if (!domainClassId.equals(that.domainClassId)) return false;
        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (displayFields != null ? !displayFields.equals(that.displayFields) : that.displayFields != null)
            return false;
        if (logicFields != null ? !logicFields.equals(that.logicFields) : that.logicFields != null) return false;
        if (condition != null ? !condition.equals(that.condition) : that.condition != null) return false;
        if (groupBy != null ? !groupBy.equals(that.groupBy) : that.groupBy != null) return false;
        if (having != null ? !having.equals(that.having) : that.having != null) return false;
        if (orderBy != null ? !orderBy.equals(that.orderBy) : that.orderBy != null) return false;
        return limit != null ? limit.equals(that.limit) : that.limit == null;

    }

    @Override
    public int hashCode() {
        int result = domainClassId.hashCode();
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (displayFields != null ? displayFields.hashCode() : 0);
        result = 31 * result + (logicFields != null ? logicFields.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (groupBy != null ? groupBy.hashCode() : 0);
        result = 31 * result + (having != null ? having.hashCode() : 0);
        result = 31 * result + (orderBy != null ? orderBy.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        return result;
    }
}
