package mongoose.client.entities.util.filters;

import mongoose.shared.entities.Filter;
import webfx.framework.client.orm.entity.filter.DqlClause;
import webfx.framework.client.orm.entity.filter.DqlStatement;
import webfx.framework.client.orm.entity.filter.DqlStatementBuilder;

public final class Filters {

    public static DqlStatement toDqlStatement(Filter filter) {
        if (filter == null)
            return null;
        DqlStatementBuilder sfb = new DqlStatementBuilder(filter.getClassId());
        sfb.setAlias(  filter.getAlias());
        sfb.setFields( filter.getFields());
        sfb.setWhere(  DqlClause.create(filter.getWhereClause()));
        sfb.setGroupBy(DqlClause.create(filter.getGroupByClause()));
        sfb.setHaving( DqlClause.create(filter.getHavingClause()));
        sfb.setOrderBy(DqlClause.create(filter.getOrderByClause()));
        sfb.setLimit(  DqlClause.create(filter.getLimitClause()));
        sfb.setColumns(filter.getColumns());
        return sfb.build();
    }

    public static String toStringJson(Filter filter) {
        return filter == null ? null : toDqlStatement(filter).toStringJson();
    }

}
