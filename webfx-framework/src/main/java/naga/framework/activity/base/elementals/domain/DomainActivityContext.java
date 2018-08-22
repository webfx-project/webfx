package naga.framework.activity.base.elementals.domain;

import naga.framework.activity.base.elementals.domain.impl.DomainActivityContextFinal;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.HasDataSourceModel;
import naga.framework.activity.ActivityContext;

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
