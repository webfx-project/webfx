package mongoose.client.entities.util.filters;

import mongoose.shared.entities.Filter;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.framework.client.ui.filter.StringFilterBuilder;

public final class Filters {

    public static StringFilter toStringFilter(Filter filter) {
        if (filter == null)
            return null;
        StringFilterBuilder sfb = new StringFilterBuilder(filter.getClassId());
        sfb.setAlias(  filter.getAlias());
        sfb.setFields( filter.getFields());
        sfb.setWhere(  filter.getWhereClause());
        sfb.setGroupBy(filter.getGroupByClause());
        sfb.setHaving( filter.getHavingClause());
        sfb.setOrderBy(filter.getOrderByClause());
        sfb.setLimit(  filter.getLimitClause());
        sfb.setColumns(filter.getColumns());
        return sfb.build();
    }

    public static String toStringJson(Filter filter) {
        return filter == null ? null : toStringFilter(filter).toStringJson();
    }

}
