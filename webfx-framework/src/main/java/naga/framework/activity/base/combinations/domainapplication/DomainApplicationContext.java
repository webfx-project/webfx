package naga.framework.activity.base.combinations.domainapplication;

import naga.framework.activity.base.elementals.domain.DomainActivityContext;
import naga.framework.activity.base.combinations.domainapplication.impl.DomainApplicationContextFinal;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.activity.base.elementals.application.ApplicationContext;

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
