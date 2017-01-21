package naga.framework.activity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface DomainActivityContext
        <THIS extends DomainActivityContext<THIS>>

        extends ActivityContext<THIS> {

    THIS setDataSourceModel(DataSourceModel dataSourceModel);

    DataSourceModel getDataSourceModel();

    static DomainActivityContextFinal create(ActivityContext parentContext) {
        return new DomainActivityContextFinal(parentContext, DomainActivityContext::create);
    }

    static DomainActivityContextFinal create(ActivityContext parentContext, DataSourceModel dataSourceModel) {
        return create(parentContext).setDataSourceModel(dataSourceModel);
    }

    static DomainActivityContextFinal create(DataSourceModel dataSourceModel) {
        return create(null, dataSourceModel);
    }

}
