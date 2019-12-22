package mongoose.client.entities.util.filters;

import mongoose.shared.entities.Filter;
import webfx.framework.shared.orm.dql.DqlClause;
import webfx.framework.shared.orm.dql.DqlStatement;
import webfx.framework.shared.orm.dql.DqlStatementBuilder;

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

}
