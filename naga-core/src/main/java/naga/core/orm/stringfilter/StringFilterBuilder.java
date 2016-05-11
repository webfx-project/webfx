package naga.core.orm.stringfilter;

import naga.core.json.JsonObject;
import naga.core.json.Json;
import naga.core.util.Strings;

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
        alias = sf.getAlias();
        fields = sf.getFields();
        where = sf.getWhere();
        groupBy = sf.getGroupBy();
        having = sf.getHaving();
        orderBy = sf.getOrderBy();
        limit = sf.getLimit();
        return this;
    }

    public StringFilterBuilder applyJson(JsonObject json) {
        if (json == null)
            return this;
        if (!isApplicable(json.getString("class")))
            throw new IllegalArgumentException();
        alias = json.getString("alias");
        fields = json.getString("fields");
        where = json.getString("where");
        groupBy = json.getString("groupBy");
        having = json.getString("having");
        orderBy = json.getString("orderBy");
        limit = json.getString("limit");
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
        if (sf.getWhere() != null)
            setWhere(where == null ? sf.getWhere() : "(" + where + ") and (" + sf.getWhere() + ")");
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

    public StringFilter build() {
        return new StringFilter(domainClassId, alias, fields, where, groupBy, having, orderBy, limit);
    }
}
