package naga.framework.activity.combinations.domainapplication;

import naga.framework.activity.domain.DomainActivityContext;
import naga.framework.activity.combinations.domainapplication.impl.DomainApplicationContextFinal;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.activity.application.ApplicationContext;

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
