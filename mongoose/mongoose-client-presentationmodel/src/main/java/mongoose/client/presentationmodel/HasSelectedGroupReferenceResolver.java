package mongoose.client.presentationmodel;

import webfx.framework.shared.orm.expression.builder.ReferenceResolver;

public interface HasSelectedGroupReferenceResolver {

    ReferenceResolver getSelectedGroupReferenceResolver();

    void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver);
}
