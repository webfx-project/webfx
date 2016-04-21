package naga.core.orm.filter;

import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public class StringFilterBuilder {
    private final Object domainClassId;
    private String alias;
    private String fields;
    private String condition;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public StringFilterBuilder() {
        domainClassId = null;
    }

    public StringFilterBuilder(Object domainClassId) {
        this.domainClassId = domainClassId;
    }

    public StringFilterBuilder(StringFilter sf) {
        domainClassId = sf.getDomainClassId();
        applyStringFilter(sf);
    }

    private boolean isApplicable(StringFilter sf) {
        boolean applicable = sf.getDomainClassId() == null || domainClassId != null && domainClassId.equals(sf.getDomainClassId());
        if (!applicable)
            System.out.println("Not applicable!!!");
        return applicable;
    }

    public StringFilterBuilder applyStringFilter(StringFilter sf) {
        if (sf == null)
            return this;
        if (!isApplicable(sf))
            throw new IllegalArgumentException();
        alias = sf.getAlias();
        fields = sf.getFields();
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
        if (!isApplicable(sf))
            throw new IllegalArgumentException("Trying to merge filters of different classes (" + domainClassId + " / " + sf.getDomainClassId() + ")");
        if (sf.getAlias() != null)
            setAlias(sf.getAlias());
        if (sf.getFields() != null)
            setFields(mergeFields(fields, sf.getFields()));
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
        return Strings.isEmpty(fields1) ? fields2 : Strings.isEmpty(fields2) ? fields1 : fields1 + ',' + fields2;
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

    public StringFilterBuilder setCondition(String condition) {
        this.condition = condition;
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

    public StringFilter build() {
        return new StringFilter(domainClassId, alias, fields, condition, groupBy, having, orderBy, limit);
    }
}
