package naga.core.orm.filter;

import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public class StringFilterBuilder {
    private final Object domainClassId;
    private String alias;
    private String displayFields;
    private String logicFields;
    private String condition;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public StringFilterBuilder(Object domainClassId) {
        this.domainClassId = domainClassId;
    }

    public StringFilterBuilder(StringFilter sf) {
        domainClassId = sf.getDomainClassId();
        applyStringFilter(sf);
    }

    public StringFilterBuilder applyStringFilter(StringFilter sf) {
        if (!Objects.equals(domainClassId, sf.getDomainClassId()))
            throw new IllegalArgumentException();
        alias = sf.getAlias();
        displayFields = sf.getDisplayFields();
        logicFields = sf.getLogicFields();
        condition = sf.getCondition();
        groupBy = sf.getGroupBy();
        having = sf.getHaving();
        orderBy = sf.getOrderBy();
        limit = sf.getLimit();
        return this;
    }

    public void merge(StringFilter sf) {
        if (sf == null)
            return;
        if (!Objects.equals(domainClassId, sf.getDomainClassId()))
            throw new IllegalArgumentException("Trying to merge filters of different classes (" + domainClassId + " / " + sf.getDomainClassId() + ")");
        if (sf.getAlias() != null)
            setAlias(sf.getAlias());
        if (sf.getDisplayFields() != null)
            setDisplayFields(sf.getDisplayFields());
        if (sf.getLogicFields() != null)
            setLogicFields(mergeFields(logicFields, sf.getLogicFields()));
        if (sf.getCondition() != null)
            setCondition(condition == null ? sf.getCondition() : "(" + condition + ") and (" + sf.getCondition() + ")");
        if (sf.getGroupBy() != null)
            setGroupBy(sf.getGroupBy());
        if (sf.getHaving() != null)
            setHaving(sf.getHaving());
        if (sf.getOrderBy() != null)
            setOrderBy(sf.getOrderBy());
        if (sf.getLimit() != null)
            setLimit(sf.getLimit());
    }

    public static String mergeFields(String fields1, String fields2) {
        return fields1 == null || fields1.isEmpty() ? fields2 : fields2 == null || fields2.isEmpty() ? fields1 : fields1 + ',' + fields2;
    }

    public static StringFilter mergeStringFilters(Object... args) {
        StringFilter stringFilter = (StringFilter) args[0];
        if (args.length == 1)
            return stringFilter;
        StringFilterBuilder mergeBuilder = new StringFilterBuilder(stringFilter);
        for (int i = 1; i < args.length; i++)
            mergeBuilder.merge((StringFilter) args[i]);
        return mergeBuilder.build();
    }

    /* Fluent API Setters */

    public StringFilterBuilder setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public StringFilterBuilder setDisplayFields(String displayFields) {
        this.displayFields = displayFields;
        return this;
    }

    public StringFilterBuilder setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public StringFilterBuilder setLogicFields(String logicFields) {
        this.logicFields = logicFields;
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

    public StringFilter build() {
        return new StringFilter(domainClassId, alias, displayFields, logicFields, condition, groupBy, having, orderBy, limit);
    }
}
