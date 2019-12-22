package webfx.framework.shared.orm.expression.builder.terms;


import webfx.framework.shared.orm.expression.terms.Update;

/**
 * @author Bruno Salmon
 */
public final class UpdateBuilder extends DqlOrderBuilder<Update> {
    public Object filterId;
    public ExpressionArrayBuilder setFields;

    public UpdateBuilder() {
    }

    @Override
    protected Update buildSqlOrder() {
        propagateDomainClasses();
        return new Update(filterId, buildingClass, buildingClassAlias, definition, sqlDefinition, sqlParameters,
                setFields.build(),
                where == null ? null : where.build()
                );
    }

    @Override
    protected void propagateDomainClasses() {
        super.propagateDomainClasses();
        if (setFields != null)
            setFields.buildingClass = buildingClass;
    }
}
