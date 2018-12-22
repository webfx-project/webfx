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

    static DomainApplicationContextFinal createDomainApplicationContext() {
        return new DomainApplicationContextFinal();
    }

    static DomainApplicationContextFinal createDomainApplicationContext(DataSourceModel dataSourceModel, String[]mainArgs) {
        return createDomainApplicationContext().setDataSourceModel(dataSourceModel);
    }

}
