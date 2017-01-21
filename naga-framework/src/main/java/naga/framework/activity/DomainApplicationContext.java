package naga.framework.activity;

import naga.framework.activity.view.ViewDomainActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.client.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface DomainApplicationContext
        <THIS extends DomainApplicationContext<THIS>>

        extends ApplicationContext<THIS>,
        DomainActivityContext<THIS> {

    static DomainApplicationContextFinal create(String[] mainArgs) {
        return new DomainApplicationContextFinal(mainArgs, ViewDomainActivityContext::create);
    }

    static DomainApplicationContextFinal create(DataSourceModel dataSourceModel, String[]mainArgs) {
        return create(mainArgs).setDataSourceModel(dataSourceModel);
    }

}
