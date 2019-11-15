package mongoose.client.entities.util.filters;

import mongoose.shared.entities.Filter;
import webfx.framework.client.orm.entity.filter.EqlFilter;
import webfx.framework.client.orm.entity.filter.EqlFilterBuilder;

public final class Filters {

    public static EqlFilter toEqlFilter(Filter filter) {
        if (filter == null)
            return null;
        EqlFilterBuilder sfb = new EqlFilterBuilder(filter.getClassId());
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
        return filter == null ? null : toEqlFilter(filter).toStringJson();
    }

}
