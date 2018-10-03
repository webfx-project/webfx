package webfx.framework.client.activity.impl.combinations.domainapplication;

import webfx.framework.client.activity.impl.elementals.domain.DomainActivityContext;
import webfx.framework.client.activity.impl.combinations.domainapplication.impl.DomainApplicationContextFinal;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.client.activity.impl.elementals.application.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface DomainApplicationContext
        <THIS extends DomainApplicationContext<THIS>>

        extends ApplicationContext<THIS>,
        DomainActivityContext<THIS> {

    static DomainApplicationContextFinal createDomainApplicationContext(String[] mainArgs) {
        return new DomainApplicationContextFinal(mainArgs);
    }

    static DomainApplicationContextFinal createDomainApplicationContext(DataSourceModel dataSourceModel, String[]mainArgs) {
        return createDomainApplicationContext(mainArgs).setDataSourceModel(dataSourceModel);
    }

}
