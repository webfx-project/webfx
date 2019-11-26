package webfx.framework.shared.orm.expression.builder.terms;


import webfx.framework.shared.orm.expression.terms.Select;

/**
 * @author Bruno Salmon
 */
public final class SelectBuilder extends DqlOrderBuilder<Select> {
    public Object filterId;
    public boolean distinct = false;
    public boolean includeIdColumn = true;
    public ExpressionArrayBuilder fields;
    public ExpressionArrayBuilder groupBy;
    public ExpressionBuilder having;
    public ExpressionArrayBuilder orderBy;

    public SelectBuilder() {
    }

    @Override
    protected Select buildSqlOrder() {
        propagateDomainClasses();
        return new Select(filterId, buildingClass, buildingClassAlias, definition, sqlDefinition, sqlParameters,
                distinct,
                fields == null ? null : fields.build(),
                where == null ? null : where.build(),
                groupBy == null ? null : groupBy.build(),
                having == null ? null : having.build(),
                orderBy == null ? null : orderBy.build(),
                limit == null ? null : limit.build(),
                includeIdColumn);
    }

    @Override
    protected void propagateDomainClasses() {
        super.propagateDomainClasses();
        if (fields != null)
            fields.buildingClass = buildingClass;
        if (groupBy != null)
            groupBy.buildingClass = buildingClass;
        if (having != null)
            having.buildingClass = buildingClass;
        if (orderBy != null)
            orderBy.buildingClass = buildingClass;
    }
}
