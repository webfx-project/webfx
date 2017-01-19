package naga.framework.activity.client;

import naga.framework.activity.DomainActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.client.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface DomainApplicationContext
        <THIS extends DomainApplicationContext<THIS>>

        extends DomainActivityContext<THIS>,
        ApplicationContext<THIS> {

    static DomainApplicationContextFinal create(String[] mainArgs) {
        return new DomainApplicationContextFinal(mainArgs, UiDomainActivityContext::create);
    }

    static DomainApplicationContextFinal create(DataSourceModel dataSourceModel, String[]mainArgs) {
        return create(mainArgs).setDataSourceModel(dataSourceModel);
    }

}
