package webfx.framework.activity.impl.elementals.domain;

import webfx.framework.activity.impl.elementals.domain.impl.DomainActivityContextFinal;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.orm.domainmodel.HasDataSourceModel;
import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContext
        <THIS extends DomainActivityContext<THIS>>

        extends ActivityContext<THIS>,
        HasDataSourceModel {

    THIS setDataSourceModel(DataSourceModel dataSourceModel);

    static DomainActivityContextFinal createDomainActivityContext(ActivityContext parentContext) {
        return new DomainActivityContextFinal(parentContext, DomainActivityContext::createDomainActivityContext);
    }

    static DomainActivityContextFinal createDomainActivityContext(ActivityContext parentContext, DataSourceModel dataSourceModel) {
        return createDomainActivityContext(parentContext).setDataSourceModel(dataSourceModel);
    }

    static DomainActivityContextFinal createDomainActivityContext(DataSourceModel dataSourceModel) {
        return createDomainActivityContext(null, dataSourceModel);
    }

}
