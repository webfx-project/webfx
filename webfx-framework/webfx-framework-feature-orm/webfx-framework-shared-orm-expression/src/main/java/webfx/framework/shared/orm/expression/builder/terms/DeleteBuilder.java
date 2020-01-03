package webfx.framework.shared.orm.expression.builder.terms;


import webfx.framework.shared.orm.expression.terms.Delete;

/**
 * @author Bruno Salmon
 */
public final class DeleteBuilder extends DqlOrderBuilder<Delete> {
    public Object filterId;

    public DeleteBuilder() {
    }

    @Override
    protected Delete buildSqlOrder() {
        propagateDomainClasses();
        return new Delete(filterId, buildingClass, buildingClassAlias, definition, sqlDefinition, sqlParameters,
                where == null ? null : where.build());
    }
}
