package naga.framework.activity.client;

import naga.framework.activity.DomainActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.client.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface DomainApplicationContext<C extends DomainApplicationContext<C>> extends DomainActivityContext<C>, ApplicationContext<C> {

    static DomainApplicationContext<?> create(String[] mainArgs) {
        return new DomainApplicationContextImpl(mainArgs, UiDomainActivityContext::create);
    }

    static DomainApplicationContext<?> create(DataSourceModel dataSourceModel, String[]mainArgs) {
        return create(mainArgs).setDataSourceModel(dataSourceModel);
    }

}
