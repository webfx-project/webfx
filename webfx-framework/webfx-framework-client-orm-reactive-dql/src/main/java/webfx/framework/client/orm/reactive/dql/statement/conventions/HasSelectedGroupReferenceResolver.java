package webfx.framework.client.orm.reactive.dql.statement.conventions;

import webfx.framework.shared.orm.expression.builder.ReferenceResolver;

public interface HasSelectedGroupReferenceResolver {

    ReferenceResolver getSelectedGroupReferenceResolver();

    void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver);
}
